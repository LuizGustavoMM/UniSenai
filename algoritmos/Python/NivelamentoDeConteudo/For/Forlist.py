cidades = []
Estados=[]

tamanho_lista=int(input("Digite o tamanho da lista: "))

for elemento in range(tamanho_lista):
    cidade = input("Digite o nome da cidade: ")
    cidades.append(cidade)
    estado = input("Digite o nome do Estado: ")
    Estados.append(estado)
    print(cidades, Estados)
print("As cidades digitadas foram: ", cidades)
print("Os Estados digitados foram: ", Estados)
