num1 = int(input("Digite um primeiro número de uma operação: "))
num2 = int(input("Digite um segundo número de uma operação: "))
escolha = int(input("Digite o número da operação que deseja realizar\n1. Adição \n2. Subtração \n3. Multiplicação \n4. divisão "))
if escolha == 1:
    print("A adição desses dois números vai ser de: {}" .format(num1+num2))
elif escolha == 2:
    print("A subtração desses dois números resultará em: {}" .format(num1-num2))
elif escolha == 3:
    print("A multiplicação desses números resultará em: {}" .format(num1*num2))
elif escolha == 4:
    print("O resultado da divisão entres esses dois números será de: {}" .format(num1/num2))
else:
    print("Digite uma opção válida!!!")    