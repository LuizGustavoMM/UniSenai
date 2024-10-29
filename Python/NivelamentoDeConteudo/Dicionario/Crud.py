estoque_carros = {}

# Adicionar ou atualizar carros
while True:
    escolha = int(input('Digite o número do menu que deseja acessar!\n\n1. Adicionar carro\n2. Pesquisar carro\n3. Alterar carro\n4. Remover carro\n5. Exibir carro\n6. Sair\nR: '))
    #Tratamento de excessão
    if escolha not in (1, 2, 3, 4, 5 ,6):
        print('Digite uma opção válida!')
    if escolha == 1:
        nome = input("Digite o nome do carro a ser adicionado ou atualizado: ")
        quantidade = int(input(f"Digite a quantidade de '{nome}': "))
        preco = float(input(f"Digite o preço de '{nome}': "))
        if nome in estoque_carros:
            print(f"Carro '{nome}' já existe. Atualizando quantidade e preço.")
        else:
            print(f"Adicionando carro '{nome}'.")
        estoque_carros[nome] = {'quantidade': quantidade, 'preco': preco}
# Pesquisar um carro
    if escolha == 2:
        nome_pesquisa = input("Digite o nome do carro a ser pesquisado: ")
        if nome_pesquisa in estoque_carros:
            carro_info = estoque_carros[nome_pesquisa]
            print(f"Carro encontrado: Nome: {nome_pesquisa}, Quantidade: {carro_info['quantidade']}, Preço: {carro_info['preco']}")
        else:
            print(f"Carro '{nome_pesquisa}' não encontrado.")

# Alterar informações de um carro
    if escolha == 3:
        nome_alterar = input("Digite o nome do carro a ser alterado: ")
        if nome_alterar in estoque_carros:
            nova_quantidade = input(f"Digite a nova quantidade de '{nome_alterar}' (ou pressione Enter para manter): ")
            novo_preco = input(f"Digite o novo preço de '{nome_alterar}' (ou pressione Enter para manter): ")
        if nova_quantidade:
            estoque_carros[nome_alterar]['quantidade'] = int(nova_quantidade)
        if novo_preco:
            estoque_carros[nome_alterar]['preco'] = float(novo_preco)
            print(f"Carro '{nome_alterar}' atualizado com sucesso.")
        else:
            print(f"Carro '{nome_alterar}' não encontrado.")
    
# Remover um carro
    if escolha == 4:
        nome_remover = input("Digite o nome do carro a ser removido: ")
        if nome_remover in estoque_carros:
            del estoque_carros[nome_remover]
            print(f"Carro '{nome_remover}' removido com sucesso.")
        else:
            print(f"Carro '{nome_remover}' não encontrado.")
    
# Exibir todos os carros no estoque
    if escolha == 5:
        if not estoque_carros:
            print("Estoque vazio.")
        else:
            print("Carros no estoque:")
        for nome, info in estoque_carros.items():
            print(f"Nome: {nome}, Quantidade: {info['quantidade']}, Preço:{info['preco']}")
    
# Sair do programa    
    if escolha == 6:
        print("Saindo do programa...")
        break
