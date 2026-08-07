
print('Bem-vindo ao mercado 3 irmãos!')

cont = 0
lista = []
while cont == 0:
    produto = input('Digite quais produtos deseja comprar ou digite sair.\nR: ')
    produtoLower = produto.lower
    match produto:
        case 'sair':
            print('\nObrigado e volte sempre\n')
            cont += 1
        case _:
            lista.append(produto)
            print(lista)