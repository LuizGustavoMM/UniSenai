cont1 = 0
produtos = []
while cont1 == 0:
    menu = int(input("Digite o número da ação que deseja realizar:\n1. Adicionar item à lista.\n2. Remover item da lista\n3. Mostrar a lista\n4. Sair\nR: "))
    if menu not in [1,2,3,4]:
        print("\n\n\nOpção inválida, digite um número entre 1 e 4!\n\n\n")
    if menu == 1:
        qntd = int(input("Digite quantos produtos deseja adicionar na lista: "))
        cont2 = 0
        while cont2 != qntd:
            produto = input("Digite o nome do produto que deseja adicionar na lista: ")
            produtos.append(produto)
            cont2 += 1
    if menu == 2:
        qntd = int(input("Digite quantos produtos deseja remover da lista: "))
        cont3 = 0
        while cont3 != qntd:
            print(produtos)
            produto = input("Digite o nome do produto que deseja remover da lista: ")
            produtos.remove(produto)
            cont3 += 1
    if menu == 3:
        
        print("\n")
        print(produtos,"\n\n")
    if menu == 4:
        cont1 += 1
print("Produtos na lista {}" .format(produtos))