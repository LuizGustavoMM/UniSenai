from CalcFunc import *
opcao = 1
while opcao:
    print("0. Sair")
    print("1. Somar")
    print("2. Subtrair")
    print("3. Multiplicação")
    print("4. Divisão")
opcao = int(input("Opção: "))
match opcao:
    case 1:
        soma()
    case 2:
        subtracao()
    case 3:
        multiplicacao()
    case 4:
        divisao()
    case 0:
        sair()
    case _:
        print("Opção inexistente, escolha uma das opções informadas.")