from CalcFunc import *

opcao = 1
while opcao:
    print("0. Sair")
    print("1. Somar")
    print("2. Subtrair")
    print("3. Multiplicação")
    print("4. Divisão")
    opcao = int(input("Opção: "))

    if(opcao == 1):
        soma()
    else:
        if(opcao == 2):
            subtracao()
        else:
            if(opcao == 3):
                multiplicacao()
            else:
                if(opcao == 4):
                    divisao()
                else:
                    if(opcao == 0):
                        sair()
                    else:
                        print("Opção inexistente, escolha uma das opções informadas.")