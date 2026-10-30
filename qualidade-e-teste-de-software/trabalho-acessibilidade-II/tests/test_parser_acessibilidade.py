"""Testes unitários do parser_acessibilidade.py."""

import io
import json
import sys
from pathlib import Path

import pytest

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))

import parser_acessibilidade as parser  # noqa: E402


def _violacao(regra, impacto, tags, alvos):
    return {
        "id": regra,
        "impact": impacto,
        "tags": tags,
        "description": f"descrição de {regra}",
        "help": f"ajuda de {regra}",
        "helpUrl": f"https://dequeuniversity.com/rules/axe/4.14/{regra}",
        "nodes": [{"target": alvo, "html": f"<x data-alvo='{alvo}'>"} for alvo in alvos],
    }


@pytest.fixture
def resultado_axe():
    return {
        "testEngine": {"name": "axe-core", "version": "4.14.0"},
        "url": "http://localhost:8080/index.html",
        "timestamp": "2026-10-30T22:00:00.000Z",
        "passes": [{"id": "bypass"}],
        "incomplete": [],
        "violations": [
            _violacao("aria-deprecated-role", "minor", ["wcag2a", "wcag412"], [[".social"]]),
            _violacao("image-alt", "critical", ["cat.text-alternatives", "wcag2a", "wcag111"], [["img"], ["article > img"]]),
            _violacao("color-contrast", "serious", ["wcag2aa", "wcag143"], [[".resumo"]]),
            _violacao("meta-viewport", "moderate", ["wcag2aa", "wcag144"], [["meta"]]),
            _violacao("link-name", "serious", ["wcag2a", "wcag244", "wcag412"], [["a"], ["a:nth-child(2)"], ["a:nth-child(3)"]]),
        ],
    }


@pytest.fixture
def arquivo_cli(tmp_path, resultado_axe):
    """Arquivo no formato do axe CLI: lista com um resultado por URL."""
    caminho = tmp_path / "resultado_axe.json"
    caminho.write_text(json.dumps([resultado_axe]), encoding="utf-8")
    return caminho


class TestCarregarRelatorio:
    def test_aceita_formato_lista_do_axe_cli(self, arquivo_cli):
        resultado = parser.carregar_relatorio(arquivo_cli)
        assert resultado["url"] == "http://localhost:8080/index.html"

    def test_aceita_objeto_da_api_axe_core(self, tmp_path, resultado_axe):
        caminho = tmp_path / "api.json"
        caminho.write_text(json.dumps(resultado_axe), encoding="utf-8")
        assert len(parser.carregar_relatorio(caminho)["violations"]) == 5

    def test_arquivo_inexistente(self, tmp_path):
        with pytest.raises(parser.ErroRelatorio, match="não encontrado"):
            parser.carregar_relatorio(tmp_path / "nao_existe.json")

    def test_json_invalido(self, tmp_path):
        caminho = tmp_path / "quebrado.json"
        caminho.write_text("{ isto não é json", encoding="utf-8")
        with pytest.raises(parser.ErroRelatorio, match="JSON inválido"):
            parser.carregar_relatorio(caminho)

    def test_lista_vazia(self, tmp_path):
        caminho = tmp_path / "vazio.json"
        caminho.write_text("[]", encoding="utf-8")
        with pytest.raises(parser.ErroRelatorio, match="não contém resultados"):
            parser.carregar_relatorio(caminho)

    def test_formato_sem_violations(self, tmp_path):
        caminho = tmp_path / "outro.json"
        caminho.write_text(json.dumps({"categories": {}}), encoding="utf-8")
        with pytest.raises(parser.ErroRelatorio, match="Formato não reconhecido"):
            parser.carregar_relatorio(caminho)


class TestMapearCriteriosWcag:
    def test_criterio_de_tres_digitos(self):
        [criterio] = parser.mapear_criterios_wcag(["wcag2a", "wcag111"])
        assert (criterio.numero, criterio.nome, criterio.nivel) == ("1.1.1", "Conteúdo Não Textual", "A")

    def test_criterio_com_numero_de_dois_digitos(self):
        [criterio] = parser.mapear_criterios_wcag(["wcag21aa", "wcag1410"])
        assert (criterio.numero, criterio.nivel, criterio.versao) == ("1.4.10", "AA", "2.1")

    def test_regra_com_varios_criterios(self):
        numeros = [c.numero for c in parser.mapear_criterios_wcag(["wcag2a", "wcag244", "wcag412"])]
        assert numeros == ["2.4.4", "4.1.2"]

    def test_tags_de_nivel_e_categoria_nao_viram_criterio(self):
        assert parser.mapear_criterios_wcag(["wcag2a", "wcag21aa", "cat.color", "best-practice"]) == []

    def test_criterio_fora_da_tabela_usa_nivel_da_tag(self):
        [criterio] = parser.mapear_criterios_wcag(["wcag22aa", "wcag258"])
        assert (criterio.numero, criterio.nivel, criterio.versao) == ("2.5.8", "AA", "2.2")


class TestExtrairECategorizar:
    def test_extrai_todas_as_violacoes_com_elementos(self, resultado_axe):
        violacoes = parser.extrair_violacoes(resultado_axe)
        assert len(violacoes) == 5
        imagem = next(v for v in violacoes if v.regra == "image-alt")
        assert [e.seletor for e in imagem.elementos] == ["img", "article > img"]
        assert imagem.titulo == "Imagens sem texto alternativo"

    def test_regra_sem_traducao_usa_help_do_axe(self):
        resultado = {"violations": [_violacao("regra-nova", "serious", [], [["div"]])]}
        [violacao] = parser.extrair_violacoes(resultado)
        assert violacao.titulo == "ajuda de regra-nova"

    def test_seletor_de_iframe_aninhado(self):
        resultado = {"violations": [_violacao("button-name", "critical", [], [[["iframe#video", "button"]]])]}
        [violacao] = parser.extrair_violacoes(resultado)
        assert violacao.elementos[0].seletor == "iframe#video >>> button"

    def test_ordem_de_severidade(self, resultado_axe):
        grupos = parser.categorizar_por_severidade(parser.extrair_violacoes(resultado_axe))
        assert list(grupos) == ["critical", "serious", "moderate", "minor"]

    def test_dentro_da_severidade_ordena_por_elementos(self, resultado_axe):
        grupos = parser.categorizar_por_severidade(parser.extrair_violacoes(resultado_axe))
        assert [v.regra for v in grupos["serious"]] == ["link-name", "color-contrast"]

    def test_severidades_vazias_aparecem(self):
        grupos = parser.categorizar_por_severidade([])
        assert all(lista == [] for lista in grupos.values()) and len(grupos) == 4

    def test_agrupa_ocorrencias_por_criterio(self, resultado_axe):
        mapa = parser.agrupar_por_criterio(parser.extrair_violacoes(resultado_axe))
        por_numero = {entrada["criterio"].numero: entrada for entrada in mapa}
        assert por_numero["4.1.2"]["regras"] == ["aria-deprecated-role", "link-name"]
        assert por_numero["4.1.2"]["ocorrencias"] == 4
        assert [entrada["criterio"].numero for entrada in mapa] == ["1.1.1", "1.4.3", "1.4.4", "2.4.4", "4.1.2"]


class TestSaidas:
    def test_resumo_no_terminal(self, resultado_axe):
        grupos = parser.categorizar_por_severidade(parser.extrair_violacoes(resultado_axe))
        saida = io.StringIO()
        parser.imprimir_resumo(resultado_axe, grupos, saida=saida)
        texto = saida.getvalue()
        assert "Total: 5 regras violadas em 8 elementos" in texto
        assert "1.1.1 Conteúdo Não Textual (Nível A)" in texto
        assert "article > img" in texto

    def test_gera_pdf_valido(self, tmp_path, resultado_axe):
        grupos = parser.categorizar_por_severidade(parser.extrair_violacoes(resultado_axe))
        caminho = parser.gerar_pdf(resultado_axe, grupos, tmp_path / "saida" / "relatorio.pdf")
        assert caminho.read_bytes().startswith(b"%PDF")

    def test_gera_pdf_sem_violacoes(self, tmp_path, resultado_axe):
        resultado_axe["violations"] = []
        grupos = parser.categorizar_por_severidade([])
        caminho = parser.gerar_pdf(resultado_axe, grupos, tmp_path / "limpo.pdf")
        assert caminho.stat().st_size > 0

    def test_main_retorna_erro_para_arquivo_inexistente(self, tmp_path, capsys):
        assert parser.main([str(tmp_path / "nao_existe.json")]) == 2
        assert "Arquivo não encontrado" in capsys.readouterr().err

    def test_main_fluxo_completo(self, arquivo_cli, tmp_path):
        destino = tmp_path / "final.pdf"
        assert parser.main([str(arquivo_cli), "--pdf", str(destino)]) == 0
        assert destino.exists()
