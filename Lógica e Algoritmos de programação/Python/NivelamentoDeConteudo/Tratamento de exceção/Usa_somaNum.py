from Tratamento import somaNum

a=int(input('Digite um número: '))
b=int(input('Digite outro número: '))
try:
    c=somaNum(a,b)
    print('A soma dos dois número é: ', c)
except OverflowError:
    print('Você digitou um valor elevado para o primeiro número!')