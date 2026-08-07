estoque_remedio = ['Paracetamol', 'Ibuprofeno', 'Dipirona', 'Omeprazol', 'Loratadina', 'Amoxicilina', 'Metformina', 'Losartana', 'Salbutamol', 'Diclofenaco']
precos = ['12.99', '20.99', '35.99', '10.99', '18.50', '33.55', '60.80', '90.99', '8.99', '10.50']
carrinho = []

while True:
    print('\nRemédios disponíveis: ')
    for indice, remedio in enumerate(estoque_remedio):
        print(f"{remedio}\t R$ {precos[indice]}")

    escolha_remedio = input('\nDigite o nome do remédio que deseja reservar ou digite "Sair" para fechar o programa.\n\nResposta: ').strip()

    if escolha_remedio.lower() == 'sair':
        break

    if not estoque_remedio:
        print('Não há mais remédios disponíveis.')
        break

    # Normaliza entrada para evitar problemas de maiúsculas/minúsculas
    escolha_normalizada = escolha_remedio.lower()

    # Busca remédio no estoque
    encontrado = False
    for indice, remedio in enumerate(estoque_remedio):
        if remedio.lower() == escolha_normalizada:
            encontrado = True
            print(f'Remédio "{remedio}" reservado com sucesso!')
            carrinho.append(remedio)
            # Remove remédio do estoque e seu preço correspondente
            estoque_remedio.pop(indice)
            precos.pop(indice)
            break

    if not encontrado:
        print('Remédio indisponível no estoque! Verifique o nome digitado.')

print('\nRemédios reservados:', carrinho)
