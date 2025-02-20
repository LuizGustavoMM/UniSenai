notas = []

for i in range(5):
    nota = float(input('Digite as notas: '))
    soma = soma + nota
print('Suas notas foram {} e a média é: {}' .format(notas, soma/5))
