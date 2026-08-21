import pytest
from carrinho import CarrinhoDeCompras


# Fluxo principal: sem desconto, com frete padrão
def test_compra_sem_desconto_com_frete_padrao():
    carrinho = CarrinhoDeCompras()
    carrinho.adicionar_item("Livro", 50.0)
    assert carrinho.calcular_desconto() == 0.0
    assert carrinho.calcular_frete() == 20.0
    assert carrinho.calcular_total_final() == 70.0


# Valor limite: faixa de desconto de 10%
def test_sem_desconto_com_total_de_99_99():
    carrinho = CarrinhoDeCompras()
    carrinho.adicionar_item("Item", 99.99)
    assert carrinho.calcular_desconto() == 0.0


def test_desconto_de_10_por_cento_com_total_de_100():
    carrinho = CarrinhoDeCompras()
    carrinho.adicionar_item("Item", 100.0)
    assert carrinho.calcular_desconto() == pytest.approx(10.0)


# Valor limite: faixa de desconto de 20%
def test_desconto_de_10_por_cento_com_total_de_499_99():
    carrinho = CarrinhoDeCompras()
    carrinho.adicionar_item("Item", 499.99)
    assert carrinho.calcular_desconto() == pytest.approx(49.999)


def test_desconto_de_20_por_cento_com_total_de_500():
    carrinho = CarrinhoDeCompras()
    carrinho.adicionar_item("Item", 500.0)
    assert carrinho.calcular_desconto() == pytest.approx(100.0)


# Valor limite: faixa de frete grátis
def test_frete_cobrado_com_total_de_199_99():
    carrinho = CarrinhoDeCompras()
    carrinho.adicionar_item("Item", 199.99)
    assert carrinho.calcular_frete() == 20.0


def test_frete_gratis_com_total_de_200():
    carrinho = CarrinhoDeCompras()
    carrinho.adicionar_item("Item", 200.0)
    assert carrinho.calcular_frete() == 0.0
