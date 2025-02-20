print("Digite dois produtos e saiba qual é mais caro")
produto1 = float(input("Digite o preço do seu primeiro produto\nR$:"))
produto2 = float(input("Digite o preço do segundo produto\nR$:"))
mesmo_preco = produto1 == produto2
preco_maior = produto1 > produto2
preco_menor = produto2 > produto1

print("Os produtos tem o mesmo preço: {}" .format(mesmo_preco))
print("O primeiro produto é mais caro: {}" .format(preco_maior))
print("O segundo preço é maior: {}" .format(preco_menor))