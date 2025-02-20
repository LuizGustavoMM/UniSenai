'''
Foram realizadas 4 melhorias principais no código.
    
 1. Variavéis com nomes claros e em norma
 2. Cometários em todas operações
 3. Zerar as variáveis ao iniciar o código
 4. Refatoração do código aplicando as boas
    práticas a cima
'''


#Início do código

#Zerando as variáveis
Num1 = 0
Num2 = 0

#Explicando ao usuário qual operação será realizada
print("Digite dois números e saiba qual é o maior")
#Permitindo o input de números
Num1 = int(input("Digite o pimeiro número: "))
Num2 = int(input("Digite o segundo número: "))

#Operação entre os números
ResultadoMaior = (Num1 > Num2) * "O primeiro número é maior. {}" .format(Num1)
ResultadoMenor = (Num1 < Num2) * "O segundo número é maior. {}" .format(Num2)
ResultadoIgual = (Num1 == Num2) * "Os números são iguais."

#Resultado
print(ResultadoMaior)
print(ResultadoMenor)
print(ResultadoIgual)