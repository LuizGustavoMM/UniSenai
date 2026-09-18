"""
Atividade Prática: Auditoria de Acessibilidade
Qualidade e Teste de Software

Varre cinco sites de notícias com axe-selenium-python e grava um arquivo JSON
por site com o resultado da análise (Questão 1).

No mesmo carregamento de página o script também coleta as evidências dos
testes manuais pedidos nas Questões 3 e 4, para que as observações do
relatório sejam medidas e não impressões:

    - navegação por Tab: qual elemento recebe o foco a cada tabulação, se o
      indicador de foco é visível e se a sequência fica presa em algum ponto;
    - zoom de 200%: a janela é reduzida para 640 px de largura, o equivalente
      a 200% de zoom em uma tela de 1280 px, e o script mede se a página
      passa a exigir rolagem horizontal e o que sai da área visível;
    - estrutura lida pelo leitor de tela: landmarks, link de pular para o
      conteúdo, rótulo do campo de busca, hierarquia de títulos e idioma.

Uso:
    pip install -r requirements.txt
    python auditoria_acessibilidade.py
"""

import json
import os
import sys
import time
from datetime import datetime

from axe_selenium_python import Axe
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.common.keys import Keys


class Tee:
    """Escreve na tela e no arquivo de log ao mesmo tempo."""

    def __init__(self, caminho):
        self.terminal = sys.stdout
        self.arquivo = open(caminho, "w", encoding="utf-8")

    def write(self, texto):
        self.terminal.write(texto)
        self.arquivo.write(texto)
        self.arquivo.flush()

    def flush(self):
        self.terminal.flush()
        self.arquivo.flush()


# ---------------------------------------------------------------- config ---

SITES = [
    ("site1", "ICL Notícias", "https://iclnoticias.com.br/"),
    ("site2", "G1", "https://g1.globo.com/"),
    ("site3", "CNN Brasil", "https://www.cnnbrasil.com.br/"),
    ("site4", "UOL", "https://www.uol.com.br/"),
    ("site5", "Globo.com", "https://www.globo.com/"),
]

SAIDA = os.path.dirname(os.path.abspath(__file__))

LARGURA_NORMAL = 1280
ALTURA_NORMAL = 900
# 200% de zoom em 1280 px deixa a area util com 640 px de largura em CSS.
LARGURA_ZOOM_200 = 640

ESPERA_CARGA = 8      # segundos de espera apos o load, sites de noticia sao pesados
TABULACOES = 40       # quantas vezes a tecla Tab e pressionada por site


# ------------------------------------------------------------ javascript ---

JS_ELEMENTO_FOCADO = """
const el = document.activeElement;
if (!el || el === document.body) {
  return { vazio: true };
}
const est = window.getComputedStyle(el);
const r = el.getBoundingClientRect();
const contorno = est.outlineStyle !== 'none' && parseFloat(est.outlineWidth) > 0;
const sombra = est.boxShadow && est.boxShadow !== 'none';
const borda = parseFloat(est.borderWidth || 0) > 0;
return {
  vazio: false,
  tag: el.tagName.toLowerCase(),
  tipo: el.getAttribute('type') || '',
  classe: (el.getAttribute('class') || '').slice(0, 60),
  texto: (el.innerText || el.value || el.getAttribute('aria-label') || '').trim().slice(0, 60),
  href: (el.getAttribute('href') || '').slice(0, 80),
  focoVisivel: contorno || sombra,
  outline: est.outlineStyle + ' ' + est.outlineWidth + ' ' + est.outlineColor,
  boxShadow: sombra ? est.boxShadow.slice(0, 60) : 'none',
  temBorda: borda,
  dentroDaTela: r.top >= 0 && r.bottom <= window.innerHeight && r.width > 0 && r.height > 0,
  alturaZero: r.width === 0 || r.height === 0,
  dentroDeDialogo: !!el.closest('[role="dialog"], [aria-modal="true"], dialog')
};
"""

JS_ESTRUTURA = """
const sel = (s) => Array.from(document.querySelectorAll(s));
const visivel = (el) => {
  const e = window.getComputedStyle(el);
  const r = el.getBoundingClientRect();
  return e.display !== 'none' && e.visibility !== 'hidden' && r.width > 0 && r.height > 0;
};

const titulos = sel('h1, h2, h3, h4, h5, h6').map(h => ({
  nivel: Number(h.tagName[1]),
  texto: (h.innerText || '').trim().slice(0, 70)
}));

let saltoDeNivel = null;
for (let i = 1; i < titulos.length; i++) {
  if (titulos[i].nivel - titulos[i - 1].nivel > 1) {
    saltoDeNivel = 'h' + titulos[i - 1].nivel + ' seguido de h' + titulos[i].nivel;
    break;
  }
}

const buscas = sel('input[type=search], input[name*=busca i], input[name*=search i], input[placeholder*=busc i], [role=search] input');
const campoBusca = buscas.length ? buscas[0] : null;
let infoBusca = { existe: false };
if (campoBusca) {
  const id = campoBusca.getAttribute('id');
  const label = id ? document.querySelector('label[for="' + CSS.escape(id) + '"]') : null;
  infoBusca = {
    existe: true,
    temLabel: !!label,
    ariaLabel: campoBusca.getAttribute('aria-label') || null,
    ariaLabelledby: campoBusca.getAttribute('aria-labelledby') || null,
    title: campoBusca.getAttribute('title') || null,
    placeholder: campoBusca.getAttribute('placeholder') || null,
    dentroDeRoleSearch: !!campoBusca.closest('[role=search], search'),
    visivel: visivel(campoBusca)
  };
}

const primeiroFocavel = sel('a[href], button, input, [tabindex]:not([tabindex="-1"])')[0];
const textoPrimeiro = primeiroFocavel
  ? (primeiroFocavel.innerText || primeiroFocavel.getAttribute('aria-label') || '').trim().slice(0, 60)
  : '';
const pareceLinkDePular = /pular|ir para|conte|skip|main/i.test(textoPrimeiro);

return {
  lang: document.documentElement.getAttribute('lang'),
  titlePagina: (document.title || '').slice(0, 90),
  landmarks: {
    header: sel('header, [role=banner]').length,
    nav: sel('nav, [role=navigation]').length,
    main: sel('main, [role=main]').length,
    footer: sel('footer, [role=contentinfo]').length,
    search: sel('[role=search], search').length,
    aside: sel('aside, [role=complementary]').length
  },
  h1: sel('h1').map(h => (h.innerText || '').trim().slice(0, 70)),
  totalTitulos: titulos.length,
  saltoDeNivel: saltoDeNivel,
  primeirosTitulos: titulos.slice(0, 12),
  linkDePular: { texto: textoPrimeiro, pareceLinkDePular: pareceLinkDePular },
  campoBusca: infoBusca,
  imagens: {
    total: sel('img').length,
    semAlt: sel('img:not([alt])').length,
    altVazio: sel('img[alt=""]').length
  },
  linksSemTexto: sel('a[href]').filter(a =>
    visivel(a) &&
    !(a.innerText || '').trim() &&
    !a.getAttribute('aria-label') &&
    !a.getAttribute('title') &&
    !a.querySelector('img[alt]:not([alt=""])')
  ).length,
  botoesSemNome: sel('button').filter(b =>
    visivel(b) &&
    !(b.innerText || '').trim() &&
    !b.getAttribute('aria-label') &&
    !b.getAttribute('title')
  ).length,
  iframesSemTitle: sel('iframe:not([title])').length,
  tabindexPositivo: sel('[tabindex]').filter(e => Number(e.getAttribute('tabindex')) > 0).length,
  dialogoAberto: sel('[role=dialog], [aria-modal=true], dialog[open]').filter(visivel).length,
  totalFocaveis: sel('a[href], button, input, select, textarea, [tabindex]:not([tabindex="-1"])').filter(visivel).length
};
"""

JS_LAYOUT = """
const doc = document.documentElement;
const largura = window.innerWidth;
const sel = (s) => Array.from(document.querySelectorAll(s));
const visivel = (el) => {
  const e = window.getComputedStyle(el);
  const r = el.getBoundingClientRect();
  return e.display !== 'none' && e.visibility !== 'hidden' && r.width > 0 && r.height > 0;
};

const vazando = sel('a, button, input, img, h1, h2, h3, p, div, section, header, nav')
  .filter(visivel)
  .filter(el => {
    const r = el.getBoundingClientRect();
    return r.right > largura + 2 && r.width < largura * 3;
  });

const amostra = vazando.slice(0, 6).map(el => ({
  tag: el.tagName.toLowerCase(),
  classe: (el.getAttribute('class') || '').slice(0, 50),
  texto: (el.innerText || '').trim().slice(0, 45),
  sobraPx: Math.round(el.getBoundingClientRect().right - largura)
}));

const controles = sel('a[href], button, input').filter(visivel);
const controlesCortados = controles.filter(el => {
  const r = el.getBoundingClientRect();
  return r.right > largura + 2 || r.left < -2;
});

return {
  larguraJanela: largura,
  larguraConteudo: doc.scrollWidth,
  rolagemHorizontal: doc.scrollWidth > largura + 2,
  sobraHorizontalPx: Math.max(0, doc.scrollWidth - largura),
  elementosVazando: vazando.length,
  amostraVazando: amostra,
  totalControles: controles.length,
  controlesCortados: controlesCortados.length,
  amostraControlesCortados: controlesCortados.slice(0, 5).map(el => ({
    tag: el.tagName.toLowerCase(),
    texto: (el.innerText || el.getAttribute('aria-label') || '').trim().slice(0, 45),
    classe: (el.getAttribute('class') || '').slice(0, 50)
  }))
};
"""


# --------------------------------------------------------------- driver ---

def abrir_navegador():
    """Sobe o Chrome. O Selenium Manager baixa o chromedriver sozinho."""
    opcoes = Options()
    opcoes.add_argument("--headless=new")
    opcoes.add_argument("--window-size=%d,%d" % (LARGURA_NORMAL, ALTURA_NORMAL))
    opcoes.add_argument("--disable-gpu")
    opcoes.add_argument("--no-sandbox")
    opcoes.add_argument("--lang=pt-BR")
    opcoes.add_argument("--log-level=3")
    opcoes.add_argument(
        "--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0 Safari/537.36"
    )
    driver = webdriver.Chrome(options=opcoes)
    driver.set_page_load_timeout(90)
    return driver


def carregar(driver, url):
    try:
        driver.get(url)
    except Exception as erro:
        print("      aviso: o carregamento estourou o tempo (%s)" % type(erro).__name__)
    time.sleep(ESPERA_CARGA)
    # rola a pagina para disparar o conteudo que carrega sob demanda
    driver.execute_script("window.scrollTo(0, document.body.scrollHeight / 3);")
    time.sleep(2)
    driver.execute_script("window.scrollTo(0, 0);")
    time.sleep(1)


# ----------------------------------------------------------- questao 1 ---

def varrer_com_axe(driver, prefixo):
    """Executa o axe-core na pagina e grava <prefixo>_acess.json."""
    axe = Axe(driver)
    axe.inject()
    resultado = axe.run()

    caminho = os.path.join(SAIDA, "%s_acess.json" % prefixo)
    axe.write_results(resultado, caminho)

    violacoes = resultado.get("violations", [])
    print("      violacoes encontradas: %d" % len(violacoes))
    for v in violacoes[:5]:
        print("        - [%s] %s (%d ocorrencias)"
              % (v.get("impact"), v.get("id"), len(v.get("nodes", []))))
    return resultado


def resumir_violacoes(resultado, limite=8):
    """Extrai o essencial de cada violacao para montar o relatorio."""
    resumo = []
    for v in resultado.get("violations", [])[:limite]:
        nos = v.get("nodes", [])
        resumo.append({
            "id": v.get("id"),
            "impacto": v.get("impact"),
            "descricao": v.get("description"),
            "ajuda": v.get("help"),
            "url": v.get("helpUrl"),
            "criterios": [t for t in v.get("tags", []) if t.startswith("wcag")],
            "ocorrencias": len(nos),
            "exemplos": [{
                "seletor": n.get("target"),
                "html": (n.get("html") or "")[:300],
                "motivo": (n.get("failureSummary") or "")[:300]
            } for n in nos[:3]]
        })
    return resumo


# ----------------------------------------------------------- questao 3 ---

def testar_teclado(driver):
    """Percorre a pagina com Tab e registra o que recebe o foco."""
    driver.execute_script("window.scrollTo(0, 0); document.body.focus();")
    corpo = driver.find_element("tag name", "body")
    corpo.click()

    sequencia = []
    for _ in range(TABULACOES):
        ActionChains(driver).send_keys(Keys.TAB).perform()
        try:
            sequencia.append(driver.execute_script(JS_ELEMENTO_FOCADO))
        except Exception:
            sequencia.append({"vazio": True, "erro": True})

    reais = [p for p in sequencia if not p.get("vazio")]
    sem_indicador = [p for p in reais if not p.get("focoVisivel")]
    fora_da_tela = [p for p in reais if not p.get("dentroDaTela")]
    em_dialogo = [p for p in reais if p.get("dentroDeDialogo")]

    # deteccao de armadilha: o foco fica girando entre poucos elementos
    assinaturas = [
        "%s|%s|%s" % (p.get("tag"), p.get("texto"), p.get("href")) for p in reais
    ]
    distintos = len(set(assinaturas))
    armadilha = len(reais) >= 10 and distintos <= 3

    return {
        "tabulacoes": TABULACOES,
        "paradasComFoco": len(reais),
        "paradasDistintas": distintos,
        "semIndicadorVisivel": len(sem_indicador),
        "percentualSemIndicador": round(100 * len(sem_indicador) / len(reais), 1) if reais else None,
        "foraDaAreaVisivel": len(fora_da_tela),
        "paradasDentroDeDialogo": len(em_dialogo),
        "suspeitaDeArmadilha": armadilha,
        "primeirasParadas": reais[:12],
        "exemplosSemIndicador": sem_indicador[:5]
    }


def testar_zoom(driver, url):
    """Compara o layout em 1280 px e em 640 px, o equivalente a 200% de zoom."""
    driver.set_window_size(LARGURA_NORMAL, ALTURA_NORMAL)
    time.sleep(2)
    normal = driver.execute_script(JS_LAYOUT)

    driver.set_window_size(LARGURA_ZOOM_200, ALTURA_NORMAL)
    time.sleep(3)
    driver.execute_script("window.dispatchEvent(new Event('resize'));")
    time.sleep(2)
    ampliado = driver.execute_script(JS_LAYOUT)

    driver.set_window_size(LARGURA_NORMAL, ALTURA_NORMAL)
    time.sleep(1)

    return {"em1280px": normal, "em640px_zoom200": ampliado}


# ----------------------------------------------------------- execucao ---

def auditar(driver, prefixo, nome, url):
    print("\n[%s] %s" % (prefixo, nome))
    print("      %s" % url)

    carregar(driver, url)

    print("   1. rodando o axe-core ...")
    resultado_axe = varrer_com_axe(driver, prefixo)

    print("   2. analisando a estrutura da pagina ...")
    estrutura = driver.execute_script(JS_ESTRUTURA)

    print("   3. navegando com a tecla Tab ...")
    teclado = testar_teclado(driver)
    print("      paradas com foco: %d, sem indicador visivel: %d"
          % (teclado["paradasComFoco"], teclado["semIndicadorVisivel"]))

    print("   4. aplicando o equivalente a 200%% de zoom ...")
    zoom = testar_zoom(driver, url)
    print("      rolagem horizontal a 640 px: %s"
          % ("sim" if zoom["em640px_zoom200"]["rolagemHorizontal"] else "nao"))

    return {
        "prefixo": prefixo,
        "nome": nome,
        "url": url,
        "coletadoEm": datetime.now().isoformat(timespec="seconds"),
        "arquivoAxe": "%s_acess.json" % prefixo,
        "totalViolacoes": len(resultado_axe.get("violations", [])),
        "violacoes": resumir_violacoes(resultado_axe),
        "estrutura": estrutura,
        "teclado": teclado,
        "zoom": zoom
    }


def main():
    sys.stdout = Tee(os.path.join(SAIDA, "log_execucao.txt"))
    print("Iniciado em %s" % datetime.now().isoformat(timespec="seconds"))
    print("=" * 66)
    print("Auditoria de acessibilidade, %d sites" % len(SITES))
    print("=" * 66)

    driver = abrir_navegador()
    evidencias = []

    try:
        for prefixo, nome, url in SITES:
            try:
                evidencias.append(auditar(driver, prefixo, nome, url))
            except Exception as erro:
                print("      ERRO em %s: %s: %s" % (nome, type(erro).__name__, erro))
                evidencias.append({
                    "prefixo": prefixo, "nome": nome, "url": url,
                    "erro": "%s: %s" % (type(erro).__name__, erro)
                })
    finally:
        driver.quit()

    caminho = os.path.join(SAIDA, "evidencias_testes_manuais.json")
    with open(caminho, "w", encoding="utf-8") as fp:
        json.dump(evidencias, fp, ensure_ascii=False, indent=2)

    print("\n" + "=" * 66)
    print("Arquivos gerados em %s" % SAIDA)
    for prefixo, _, _ in SITES:
        print("   %s_acess.json" % prefixo)
    print("   evidencias_testes_manuais.json")
    print("   log_execucao.txt")
    print("=" * 66)
    print("CONCLUIDO em %s" % datetime.now().isoformat(timespec="seconds"))


if __name__ == "__main__":
    main()
