palavra = ""
escolha = 0


num1 = int(input("Digite um primeiro número de uma operação: "))
num2 = int(input("Digite um segundo número de uma operação: "))
escolha = int(input("Digite o número da operação que deseja realizar\n1. Adição \n2. Subtração \n3. Multiplicação \n4. divisão\nR: "))



if escolha == 1:
    palavra = "soma"
    op = num1+num2
elif escolha == 2:
    palavra = "subtração"
    op = num1-num2
elif escolha == 3:
    palavra = "multiplicação"
    op = num1*num2
else:
    if num2 != 0:
        palavra = "divisão"
        op = num1/num2
    else:
        print("A divisão por zero (0) não é possível!!!")
        op = 0
if escolha == 0:
  print("Digite uma opção válida!!!")
else:  
  print(f"Sua operação de {palavra} teve o resultado de: {op}")