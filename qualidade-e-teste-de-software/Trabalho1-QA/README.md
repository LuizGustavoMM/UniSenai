# Carrinho de Compras - Testes Unitários

Trabalho prático da disciplina Qualidade e Teste de Software, aplicando a norma
ISO/IEC 25010 no desenvolvimento de testes unitários para a classe
`CarrinhoDeCompras`, responsável pelo cálculo de descontos progressivos e
taxa de entrega de um checkout de e-commerce.

## Arquivos

- `carrinho.py`: classe com as regras de negócio do carrinho.
- `test_carrinho.py`: suíte de testes unitários (pytest).

## Regras de negócio

- Desconto de 10% para total de produtos a partir de R$ 100,00.
- Desconto de 20% para total de produtos a partir de R$ 500,00.
- Frete grátis para total de produtos a partir de R$ 200,00; abaixo disso, taxa fixa de R$ 20,00.
- Item com preço nulo ou negativo não pode ser adicionado.
- Checkout de carrinho vazio não é permitido.

## Como executar os testes

1. Instalar o pytest:

   ```bash
   pip install pytest
   ```

2. Executar a suíte a partir da raiz do repositório:

   ```bash
   pytest -v
   ```

Resultado esperado: `10 passed`.
