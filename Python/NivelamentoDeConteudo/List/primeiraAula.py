produtos = ["tv","celular","mouse","teclado","tablet","geladeira","forno"]
vendas = [1500, 1500, 350, 270, 900, 1200, 900]
produto = input("Insira o nome do produto em letra minúscula: ")
i = produtos.index(produto)
print(i)

print("As vendas de {} foram de {}" .format(produtos[1],vendas[1]))
i = produtos.index("mouse")
print("O valor de i é "+ str(i))
print("O produto da posição i é {}" .format(produtos[i]))