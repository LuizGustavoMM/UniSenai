numeros = []
cont = 0

for i in range(10):
    numero = int(input('Digite um número e saiba qual é par: '))
    numeros.append(numero)

for numeros in range(10):
    if numeros % 2 == 0:
        cont += 1

print('O total de números pares foi:', cont)
