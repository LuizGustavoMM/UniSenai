"""Parser do relatório JSON do axe-core CLI para o portal TechInclusiva.

Lê o arquivo gerado pelo axe CLI (``resultado_axe.json``), extrai as
violações, agrupa por severidade (critical, serious, moderate, minor),
lista os seletores CSS e trechos HTML afetados, mapeia cada regra para os
critérios de sucesso da WCAG 2.1 e produz:

* um resumo consolidado no terminal;
* um relatório em PDF salvo em ``relatorios/``.

Uso:
    python parser_acessibilidade.py [arquivo.json] [--pdf saida.pdf]

Exemplos:
    python parser_acessibilidade.py
    python parser_acessibilidade.py resultado_axe_corrigido.json

Integrantes: Luiz Gustavo Mella Massing e João Victor Bagatim
Unidade Curricular: Qualidade e Teste de Software (UniSENAI)
"""

import argparse
import json
import re
import sys
from collections import OrderedDict
from dataclasses import dataclass, field
from datetime import datetime
from pathlib import Path
from xml.sax.saxutils import escape

from reportlab.lib import colors
from reportlab.lib.enums import TA_CENTER
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import ParagraphStyle, getSampleStyleSheet
from reportlab.lib.units import cm
from reportlab.platypus import (
    KeepTogether,
    Paragraph,
    SimpleDocTemplate,
    Spacer,
    Table,
    TableStyle,
)

# Ordem de prioridade usada em todo o relatório: da mais grave para a mais leve.
SEVERIDADES = ("critical", "serious", "moderate", "minor")

ROTULOS_SEVERIDADE = {
    "critical": "Crítica",
    "serious": "Grave",
    "moderate": "Moderada",
    "minor": "Baixa",
}

CORES_SEVERIDADE = {
    "critical": colors.HexColor("#b71c1c"),
    "serious": colors.HexColor("#d84315"),
    "moderate": colors.HexColor("#8d6e00"),
    "minor": colors.HexColor("#1565c0"),
}

# Critérios de sucesso WCAG 2.1 níveis A e AA.
# Nomes conforme a tradução autorizada do W3C para o português do Brasil.
CRITERIOS_WCAG = {
    "1.1.1": ("Conteúdo Não Textual", "A"),
    "1.2.1": ("Apenas Áudio e Apenas Vídeo (Pré-gravado)", "A"),
    "1.2.2": ("Legendas (Pré-gravadas)", "A"),
    "1.2.3": ("Audiodescrição ou Mídia Alternativa (Pré-gravada)", "A"),
    "1.2.4": ("Legendas (Ao Vivo)", "AA"),
    "1.2.5": ("Audiodescrição (Pré-gravada)", "AA"),
    "1.3.1": ("Informações e Relações", "A"),
    "1.3.2": ("Sequência Significativa", "A"),
    "1.3.3": ("Características Sensoriais", "A"),
    "1.3.4": ("Orientação", "AA"),
    "1.3.5": ("Identificar o Propósito de Entrada", "AA"),
    "1.4.1": ("Uso de Cor", "A"),
    "1.4.2": ("Controle de Áudio", "A"),
    "1.4.3": ("Contraste (Mínimo)", "AA"),
    "1.4.4": ("Redimensionar Texto", "AA"),
    "1.4.5": ("Imagens de Texto", "AA"),
    "1.4.10": ("Refluxo", "AA"),
    "1.4.11": ("Contraste Não Textual", "AA"),
    "1.4.12": ("Espaçamento de Texto", "AA"),
    "1.4.13": ("Conteúdo em Foco por Mouse ou Teclado", "AA"),
    "2.1.1": ("Teclado", "A"),
    "2.1.2": ("Sem Bloqueio do Teclado", "A"),
    "2.1.4": ("Atalhos de Teclado por Caractere", "A"),
    "2.2.1": ("Ajustável por Temporização", "A"),
    "2.2.2": ("Pausar, Parar, Ocultar", "A"),
    "2.3.1": ("Três Flashes ou Abaixo do Limite", "A"),
    "2.4.1": ("Ignorar Blocos", "A"),
    "2.4.2": ("Página com Título", "A"),
    "2.4.3": ("Ordem do Foco", "A"),
    "2.4.4": ("Finalidade do Link (Em Contexto)", "A"),
    "2.4.5": ("Várias Formas", "AA"),
    "2.4.6": ("Cabeçalhos e Rótulos", "AA"),
    "2.4.7": ("Foco Visível", "AA"),
    "2.5.1": ("Gestos de Acionamento", "A"),
    "2.5.2": ("Cancelamento de Acionamento", "A"),
    "2.5.3": ("Rótulo no Nome", "A"),
    "2.5.4": ("Atuação por Movimento", "A"),
    "3.1.1": ("Idioma da Página", "A"),
    "3.1.2": ("Idioma das Partes", "AA"),
    "3.2.1": ("Em Foco", "A"),
    "3.2.2": ("Em Entrada", "A"),
    "3.2.3": ("Navegação Consistente", "AA"),
    "3.2.4": ("Identificação Consistente", "AA"),
    "3.3.1": ("Identificação do Erro", "A"),
    "3.3.2": ("Rótulos ou Instruções", "A"),
    "3.3.3": ("Sugestão de Erro", "AA"),
    "3.3.4": ("Prevenção de Erros (Legais, Financeiros, de Dados)", "AA"),
    "4.1.1": ("Análise (Parsing)", "A"),
    "4.1.2": ("Nome, Função, Valor", "A"),
    "4.1.3": ("Mensagens de Status", "AA"),
}

# Título em português e orientação de correção para as regras do axe mais
# comuns. Regras fora desta tabela usam o texto "help" do próprio axe.
REMEDIACOES = {
    "image-alt": (
        "Imagens sem texto alternativo",
        'Adicionar alt descritivo em imagens informativas e alt="" em imagens decorativas.',
    ),
    "input-image-alt": (
        "Botão de imagem sem texto alternativo",
        'Adicionar alt ao <input type="image"> descrevendo a ação do botão.',
    ),
    "button-name": (
        "Botão sem nome acessível",
        "Incluir texto visível no botão ou aria-label que descreva a ação.",
    ),
    "label": (
        "Campo de formulário sem rótulo",
        "Associar um <label for> ao id do campo ou usar aria-label/aria-labelledby.",
    ),
    "select-name": (
        "Lista de seleção sem nome acessível",
        "Associar um <label for> ao <select> ou usar aria-label.",
    ),
    "aria-valid-attr-value": (
        "Atributo ARIA com valor inválido",
        'Usar apenas valores permitidos pela especificação (ex.: aria-expanded="true" ou "false").',
    ),
    "aria-required-attr": (
        "Função ARIA sem atributo obrigatório",
        "Incluir os atributos ARIA exigidos pela função utilizada.",
    ),
    "aria-roles": (
        "Função ARIA inválida",
        "Usar somente funções ARIA definidas na especificação WAI-ARIA.",
    ),
    "color-contrast": (
        "Contraste de cor insuficiente",
        "Garantir razão mínima de 4.5:1 para texto normal e 3:1 para texto grande.",
    ),
    "document-title": (
        "Página sem título",
        "Incluir um elemento <title> descritivo e único dentro do <head>.",
    ),
    "link-name": (
        "Link sem texto discernível",
        "Dar texto ao link, alt à imagem interna ou aria-label que indique o destino.",
    ),
    "list": (
        "Lista com estrutura incorreta",
        "Manter apenas elementos <li> como filhos diretos de <ul> e <ol>.",
    ),
    "listitem": (
        "Item de lista fora de uma lista",
        "Envolver os elementos <li> em um <ul> ou <ol>.",
    ),
    "frame-title": (
        "Iframe sem nome acessível",
        "Adicionar atributo title descrevendo o conteúdo do iframe.",
    ),
    "aria-hidden-focus": (
        "Elemento oculto por ARIA que recebe foco",
        'Remover aria-hidden="true" de elementos focáveis ou retirá-los da ordem de tabulação.',
    ),
    "aria-command-name": (
        "Comando ARIA sem nome acessível",
        "Dar nome ao elemento com role=button/link (texto, aria-label) ou usar <button> nativo.",
    ),
    "autocomplete-valid": (
        "Atributo autocomplete inválido",
        'Usar tokens padronizados do HTML (ex.: autocomplete="name", "email").',
    ),
    "html-has-lang": (
        "Página sem idioma definido",
        'Adicionar lang ao elemento <html> (ex.: lang="pt-BR").',
    ),
    "html-lang-valid": (
        "Idioma da página inválido",
        "Usar um código de idioma BCP 47 válido no atributo lang do <html>.",
    ),
    "valid-lang": (
        "Idioma de trecho inválido",
        "Usar um código de idioma BCP 47 válido nos atributos lang internos.",
    ),
    "html-xml-lang-mismatch": (
        "Atributos lang e xml:lang divergentes",
        "Usar o mesmo idioma em lang e xml:lang ou remover o xml:lang.",
    ),
    "meta-viewport": (
        "Zoom da página bloqueado",
        "Remover user-scalable=no e maximum-scale menor que 2 da meta viewport.",
    ),
    "aria-deprecated-role": (
        "Função ARIA obsoleta",
        "Substituir a função obsoleta por elemento semântico ou função ARIA vigente.",
    ),
    "form-field-multiple-labels": (
        "Campo com múltiplos rótulos",
        "Manter um único <label> associado a cada campo de formulário.",
    ),
    "server-side-image-map": (
        "Mapa de imagem do lado do servidor",
        "Substituir o atributo ismap por mapa no cliente (<map>/<area>) ou links comuns.",
    ),
}

PADRAO_TAG_CRITERIO = re.compile(r"^wcag(\d)(\d)(\d+)$")
TAGS_NIVEL = {
    "wcag2a": ("2.0", "A"),
    "wcag2aa": ("2.0", "AA"),
    "wcag21a": ("2.1", "A"),
    "wcag21aa": ("2.1", "AA"),
    "wcag22aa": ("2.2", "AA"),
}


class ErroRelatorio(Exception):
    """Erro de leitura ou de formato do relatório JSON."""


@dataclass
class CriterioWCAG:
    """Critério de sucesso WCAG associado a uma regra do axe."""

    numero: str
    nome: str
    nivel: str
    versao: str

    def __str__(self):
        return f"{self.numero} {self.nome} (Nível {self.nivel})"


@dataclass
class Elemento:
    """Elemento HTML afetado por uma violação."""

    seletor: str
    html: str


@dataclass
class Violacao:
    """Regra do axe reprovada, com seus elementos e critérios WCAG."""

    regra: str
    impacto: str
    titulo: str
    remediacao: str
    descricao: str
    url_ajuda: str
    criterios: list = field(default_factory=list)
    elementos: list = field(default_factory=list)


def carregar_relatorio(caminho):
    """Lê o JSON do axe e devolve o resultado da página analisada.

    O axe CLI salva uma lista com um resultado por URL. A API do axe-core
    devolve o objeto diretamente. As duas formas são aceitas; quando há
    várias URLs, apenas a primeira é usada.

    Raises:
        ErroRelatorio: arquivo inexistente, JSON inválido ou formato
            sem a chave "violations".
    """
    caminho = Path(caminho)
    if not caminho.is_file():
        raise ErroRelatorio(f"Arquivo não encontrado: {caminho}")

    try:
        dados = json.loads(caminho.read_text(encoding="utf-8"))
    except json.JSONDecodeError as erro:
        raise ErroRelatorio(f"JSON inválido em {caminho}: {erro}") from erro

    if isinstance(dados, list):
        if not dados:
            raise ErroRelatorio(f"O arquivo {caminho} não contém resultados.")
        dados = dados[0]

    if not isinstance(dados, dict) or "violations" not in dados:
        raise ErroRelatorio(
            f"Formato não reconhecido em {caminho}: esperado relatório do axe-core "
            'com a chave "violations".'
        )
    return dados


def mapear_criterios_wcag(tags):
    """Converte as tags do axe em critérios de sucesso WCAG.

    Tags como ``wcag111`` ou ``wcag1410`` viram 1.1.1 e 1.4.10. O nível e a
    versão vêm da tabela oficial e, para critérios fora dela, das tags de
    nível (``wcag2a``, ``wcag21aa`` etc.).
    """
    versao_tag, nivel_tag = "2.x", "?"
    for tag in tags:
        if tag in TAGS_NIVEL:
            versao_tag, nivel_tag = TAGS_NIVEL[tag]

    criterios = []
    for tag in tags:
        correspondencia = PADRAO_TAG_CRITERIO.match(tag)
        if not correspondencia:
            continue
        numero = ".".join(correspondencia.groups())
        nome, nivel = CRITERIOS_WCAG.get(numero, ("Critério fora da WCAG 2.1 A/AA", nivel_tag))
        criterios.append(CriterioWCAG(numero, nome, nivel, versao_tag))
    return criterios


def _formatar_seletor(alvo):
    """Junta o seletor do axe em texto.

    Elementos dentro de iframes ou shadow DOM chegam como listas aninhadas.
    """
    partes = []
    for parte in alvo:
        partes.append(" >>> ".join(parte) if isinstance(parte, list) else str(parte))
    return " | ".join(partes)


def extrair_violacoes(resultado):
    """Monta a lista de violações a partir do resultado do axe."""
    violacoes = []
    for item in resultado.get("violations", []):
        titulo, remediacao = REMEDIACOES.get(item["id"], (item.get("help", item["id"]), item.get("help", "")))
        elementos = [
            Elemento(_formatar_seletor(no.get("target", [])), no.get("html", "").strip())
            for no in item.get("nodes", [])
        ]
        violacoes.append(
            Violacao(
                regra=item["id"],
                impacto=item.get("impact") or "minor",
                titulo=titulo,
                remediacao=remediacao,
                descricao=item.get("description", ""),
                url_ajuda=item.get("helpUrl", ""),
                criterios=mapear_criterios_wcag(item.get("tags", [])),
                elementos=elementos,
            )
        )
    return violacoes


def categorizar_por_severidade(violacoes):
    """Agrupa as violações por severidade, da mais grave para a mais leve.

    Todas as severidades aparecem no resultado, mesmo sem ocorrências, para
    que o resumo mostre explicitamente os níveis zerados.
    """
    grupos = OrderedDict((severidade, []) for severidade in SEVERIDADES)
    for violacao in violacoes:
        grupos.setdefault(violacao.impacto, []).append(violacao)
    for lista in grupos.values():
        lista.sort(key=lambda v: (-len(v.elementos), v.regra))
    return grupos


def agrupar_por_criterio(violacoes):
    """Relaciona cada critério WCAG às regras do axe que o violaram."""
    mapa = {}
    for violacao in violacoes:
        for criterio in violacao.criterios:
            entrada = mapa.setdefault(criterio.numero, {"criterio": criterio, "regras": [], "ocorrencias": 0})
            entrada["regras"].append(violacao.regra)
            entrada["ocorrencias"] += len(violacao.elementos)
    chave = lambda numero: tuple(int(parte) for parte in numero.split("."))
    return [mapa[numero] for numero in sorted(mapa, key=chave)]


def imprimir_resumo(resultado, grupos, saida=sys.stdout):
    """Exibe no terminal o resumo consolidado da auditoria."""
    total_regras = sum(len(lista) for lista in grupos.values())
    total_elementos = sum(len(v.elementos) for lista in grupos.values() for v in lista)

    linha = "=" * 78
    print(linha, file=saida)
    print("RELATÓRIO DE ACESSIBILIDADE - PORTAL TECHINCLUSIVA (WCAG 2.1 A/AA)", file=saida)
    print(linha, file=saida)
    print(f"URL analisada : {resultado.get('url', '-')}", file=saida)
    print(f"Data da coleta: {resultado.get('timestamp', '-')}", file=saida)
    motor = resultado.get("testEngine", {})
    print(f"Ferramenta    : {motor.get('name', 'axe-core')} {motor.get('version', '')}", file=saida)
    print(
        f"Regras: {len(resultado.get('passes', []))} aprovadas, {total_regras} reprovadas, "
        f"{len(resultado.get('incomplete', []))} para revisão manual",
        file=saida,
    )
    print(f"Total: {total_regras} regras violadas em {total_elementos} elementos", file=saida)
    print(linha, file=saida)

    print(f"{'Severidade':<22}{'Regras':>8}{'Elementos':>12}", file=saida)
    for severidade, lista in grupos.items():
        rotulo = f"{severidade} ({ROTULOS_SEVERIDADE.get(severidade, severidade)})"
        print(f"{rotulo:<22}{len(lista):>8}{sum(len(v.elementos) for v in lista):>12}", file=saida)
    print(linha, file=saida)

    for severidade, lista in grupos.items():
        if not lista:
            continue
        print(f"\n[{severidade.upper()}]", file=saida)
        for violacao in lista:
            criterios = ", ".join(str(c) for c in violacao.criterios) or "Sem critério WCAG associado"
            print(f"  - {violacao.regra}: {violacao.titulo} ({len(violacao.elementos)} elemento(s))", file=saida)
            print(f"    WCAG: {criterios}", file=saida)
            print(f"    Correção: {violacao.remediacao}", file=saida)
            for elemento in violacao.elementos:
                print(f"      * {elemento.seletor}", file=saida)

    if total_regras == 0:
        print("\nNenhuma violação encontrada nas regras WCAG 2.1 A/AA avaliadas.", file=saida)


def _estilos():
    base = getSampleStyleSheet()
    return {
        "titulo": ParagraphStyle("titulo", parent=base["Title"], fontSize=17, leading=21, textColor=colors.HexColor("#0b3d91")),
        "subtitulo": ParagraphStyle("subtitulo", parent=base["Normal"], alignment=TA_CENTER, fontSize=10, textColor=colors.HexColor("#444444")),
        "h2": ParagraphStyle("h2", parent=base["Heading2"], fontSize=13, textColor=colors.HexColor("#0b3d91"), spaceBefore=12),
        "h3": ParagraphStyle("h3", parent=base["Heading3"], fontSize=11, spaceBefore=8, spaceAfter=2),
        "normal": ParagraphStyle("normal", parent=base["Normal"], fontSize=9, leading=12),
        "celula": ParagraphStyle("celula", parent=base["Normal"], fontSize=8, leading=10),
        "codigo": ParagraphStyle("codigo", parent=base["Normal"], fontName="Courier", fontSize=7, leading=9),
    }


def _tabela(dados, larguras, cor_cabecalho=colors.HexColor("#0b3d91")):
    tabela = Table(dados, colWidths=larguras, repeatRows=1)
    tabela.setStyle(
        TableStyle(
            [
                ("BACKGROUND", (0, 0), (-1, 0), cor_cabecalho),
                ("TEXTCOLOR", (0, 0), (-1, 0), colors.white),
                ("FONTNAME", (0, 0), (-1, 0), "Helvetica-Bold"),
                ("FONTSIZE", (0, 0), (-1, -1), 8),
                ("GRID", (0, 0), (-1, -1), 0.4, colors.HexColor("#bbbbbb")),
                ("VALIGN", (0, 0), (-1, -1), "TOP"),
                ("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.white, colors.HexColor("#f4f6fa")]),
            ]
        )
    )
    return tabela


def _truncar(texto, limite=260):
    return texto if len(texto) <= limite else texto[: limite - 3] + "..."


def gerar_pdf(resultado, grupos, caminho_pdf, arquivo_origem=""):
    """Gera o relatório de violações em PDF e devolve o caminho salvo."""
    caminho_pdf = Path(caminho_pdf)
    caminho_pdf.parent.mkdir(parents=True, exist_ok=True)
    estilos = _estilos()
    violacoes = [v for lista in grupos.values() for v in lista]
    total_elementos = sum(len(v.elementos) for v in violacoes)
    motor = resultado.get("testEngine", {})

    doc = SimpleDocTemplate(
        str(caminho_pdf),
        pagesize=A4,
        leftMargin=1.8 * cm,
        rightMargin=1.8 * cm,
        topMargin=1.6 * cm,
        bottomMargin=1.6 * cm,
        title="Relatório de Acessibilidade - Portal TechInclusiva",
        author="Luiz Gustavo Mella Massing e João Victor Bagatim",
    )
    largura = doc.width
    conteudo = [
        Paragraph("Relatório de Violações de Acessibilidade", estilos["titulo"]),
        Paragraph("Portal TechInclusiva · WCAG 2.1 níveis A e AA · axe-core CLI", estilos["subtitulo"]),
        Spacer(1, 10),
    ]

    conteudo.append(Paragraph("1. Dados da varredura", estilos["h2"]))
    dados_varredura = [
        ["Item", "Valor"],
        ["URL analisada", resultado.get("url", "-")],
        ["Data da coleta", resultado.get("timestamp", "-")],
        ["Ferramenta", f"{motor.get('name', 'axe-core')} {motor.get('version', '')}"],
        ["Arquivo de origem", arquivo_origem or "-"],
        ["Regras aprovadas", str(len(resultado.get("passes", [])))],
        ["Regras reprovadas", str(len(violacoes))],
        ["Regras para revisão manual", str(len(resultado.get("incomplete", [])))],
        ["Elementos afetados", str(total_elementos)],
        ["Relatório gerado em", datetime.now().strftime("%d/%m/%Y %H:%M")],
    ]
    conteudo.append(_tabela(dados_varredura, [5 * cm, largura - 5 * cm]))

    conteudo.append(Paragraph("2. Resumo por severidade", estilos["h2"]))
    dados_resumo = [["Severidade", "Regras violadas", "Elementos afetados"]]
    for severidade, lista in grupos.items():
        dados_resumo.append(
            [
                f"{severidade} ({ROTULOS_SEVERIDADE.get(severidade, severidade)})",
                str(len(lista)),
                str(sum(len(v.elementos) for v in lista)),
            ]
        )
    dados_resumo.append(["Total", str(len(violacoes)), str(total_elementos)])
    tabela_resumo = _tabela(dados_resumo, [6 * cm, 4 * cm, 4 * cm])
    tabela_resumo.setStyle(TableStyle([("FONTNAME", (0, -1), (-1, -1), "Helvetica-Bold")]))
    conteudo.append(tabela_resumo)

    if not violacoes:
        conteudo.append(Spacer(1, 10))
        conteudo.append(
            Paragraph(
                "Nenhuma violação foi encontrada nas regras WCAG 2.1 A/AA avaliadas pelo axe-core.",
                estilos["normal"],
            )
        )
        doc.build(conteudo)
        return caminho_pdf

    conteudo.append(Paragraph("3. Mapeamento para critérios WCAG 2.1", estilos["h2"]))
    dados_wcag = [["Critério", "Nível", "Regras do axe", "Elementos"]]
    for entrada in agrupar_por_criterio(violacoes):
        criterio = entrada["criterio"]
        dados_wcag.append(
            [
                Paragraph(f"{criterio.numero} {escape(criterio.nome)}", estilos["celula"]),
                criterio.nivel,
                Paragraph(escape(", ".join(entrada["regras"])), estilos["celula"]),
                str(entrada["ocorrencias"]),
            ]
        )
    conteudo.append(_tabela(dados_wcag, [6.5 * cm, 1.5 * cm, largura - 10.5 * cm, 2.5 * cm]))

    # O título da seção entra no primeiro bloco para não ficar sozinho no fim da página.
    titulo_secao = [Paragraph("4. Violações detalhadas por severidade", estilos["h2"])]
    for severidade, lista in grupos.items():
        if not lista:
            continue
        cor = CORES_SEVERIDADE.get(severidade, colors.grey)
        for violacao in lista:
            criterios = "<br/>".join(escape(str(c)) for c in violacao.criterios) or "Sem critério WCAG associado"
            cabecalho = Paragraph(
                f'<font color="{cor.hexval().replace("0x", "#")}">[{severidade.upper()}]</font> '
                f"{escape(violacao.regra)}: {escape(violacao.titulo)}",
                estilos["h3"],
            )
            ficha = _tabela(
                [
                    ["Campo", "Detalhe"],
                    ["Critérios WCAG", Paragraph(criterios, estilos["celula"])],
                    ["Descrição (axe)", Paragraph(escape(violacao.descricao), estilos["celula"])],
                    ["Como corrigir", Paragraph(escape(violacao.remediacao), estilos["celula"])],
                    ["Referência", Paragraph(escape(violacao.url_ajuda), estilos["celula"])],
                ],
                [3.5 * cm, largura - 3.5 * cm],
                cor_cabecalho=cor,
            )
            dados_elementos = [["#", "Seletor CSS", "Trecho HTML"]]
            for indice, elemento in enumerate(violacao.elementos, start=1):
                dados_elementos.append(
                    [
                        str(indice),
                        Paragraph(escape(elemento.seletor), estilos["codigo"]),
                        Paragraph(escape(_truncar(elemento.html)), estilos["codigo"]),
                    ]
                )
            elementos = _tabela(dados_elementos, [0.8 * cm, 5.2 * cm, largura - 6 * cm], cor_cabecalho=colors.HexColor("#555555"))
            conteudo.append(KeepTogether(titulo_secao + [cabecalho, ficha, Spacer(1, 4), elementos]))
            titulo_secao = []
            conteudo.append(Spacer(1, 8))

    doc.build(conteudo)
    return caminho_pdf


def _ler_argumentos(argv=None):
    parser = argparse.ArgumentParser(description="Analisa o JSON do axe-core e gera relatório de acessibilidade.")
    parser.add_argument("arquivo", nargs="?", default="resultado_axe.json", help="JSON gerado pelo axe CLI (padrão: resultado_axe.json)")
    parser.add_argument("--pdf", help="caminho do PDF de saída (padrão: relatorios/relatorio_<arquivo>.pdf)")
    return parser.parse_args(argv)


def main(argv=None):
    """Executa leitura, extração, categorização, resumo e geração do PDF."""
    if hasattr(sys.stdout, "reconfigure"):
        sys.stdout.reconfigure(encoding="utf-8")
    argumentos = _ler_argumentos(argv)

    try:
        resultado = carregar_relatorio(argumentos.arquivo)
    except ErroRelatorio as erro:
        print(f"Erro: {erro}", file=sys.stderr)
        return 2

    grupos = categorizar_por_severidade(extrair_violacoes(resultado))
    imprimir_resumo(resultado, grupos)

    destino = argumentos.pdf or Path("relatorios") / f"relatorio_{Path(argumentos.arquivo).stem}.pdf"
    caminho = gerar_pdf(resultado, grupos, destino, arquivo_origem=Path(argumentos.arquivo).name)
    print(f"\nRelatório PDF salvo em: {caminho}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
