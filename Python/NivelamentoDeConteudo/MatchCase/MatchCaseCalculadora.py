num1 = float(input("Digite dois números para realizar um cálculo de sua escolha.\nPrimeiro número: "))
num2 = float(input("\nDigite o segundo número: "))

operacao = int(input("Digite o número da operação que deseja\n1. Soma\n2. Subtração\n3. Multiplicação\n4. Divisão"))

match operacao:
    case 1:
        print("A soma dos dois números é: ",num1 + num2)
    case 2:
        print("A subtração dos dois números é: ",num1 - num2)
    case 3:
        print("A multiplicação dos dois número é: ",num1 * num2)
    case 4:
        if num2 != 0:
            print("A divisão dos dois números é: ",num1 / num2)
        else:
            print("Erro! Divisão por zero")
    case _:
        print("Input inválido!")