estoque_carros = ['fusca', 'caravan', 'passat', 'gol', 'kombi', 'corcel', 'escort', 'opala', 'parati', 'uno mille']
carros_reservados = []
tamanho_lista = len(estoque_carros)

while True:
    print('Carros disponíveis: ')
    for indice, carro in enumerate(estoque_carros):
            print(f'{indice} - {carro}')
            
    escolha_carro = input('\nDigite o índice do carro que deseja reservar ou digite "Sair" para fechar o programa.\n\nResposta: ')

    if escolha_carro.lower() == 'sair':
        break
    if tamanho_lista < 1:
        print('Não há mais carros disponíveis.')
    try:
        indice_escolhido = int(escolha_carro)
        if indice_escolhido < 0 or indice_escolhido > tamanho_lista:
             print('Indice inválido, digite um número válido!')
        carro_reservado = estoque_carros.pop(indice_escolhido)
        carros_reservados.append(carro_reservado)
    
    except IndexError:
        print('Número indisponível no índice')
print('Carros Reservados: ', carros_reservados)