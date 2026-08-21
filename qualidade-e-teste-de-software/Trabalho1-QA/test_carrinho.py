import pytest
from carrinho import CarrinhoDeCompras


# Fluxo principal: sem desconto, com frete padrão
def test_compra_sem_desconto_com_frete_padrao():
    carrinho = CarrinhoDeCompras()
    carrinho.adicionar_item("Livro", 50.0)
    assert carrinho.calcular_desconto() == 0.0
    assert carrinho.calcular_frete() == 20.0
    assert carrinho.calcular_total_final() == 70.0
