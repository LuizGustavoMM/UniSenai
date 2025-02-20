def retangulo(lado_a, lado_b):
 """Calcula a área de um retângulo"""
 area = lado_a * lado_b
 return area

def triangulo(base, altura):
 """Calcula a área de um triângulo"""
 area = (base * altura) / 2
 return area

def circulo(raio):
 """Calcula a área de um círculo"""
 area = 3.14 * (raio ** 2)
 return area

while True:
    escolha = int(input('Digite qual opção deseja calcular a área:\n\n1. Retangulo\n2. Triagulo\n3. Circulo\n4. Sair\nR: '))


    if escolha == 1:
        ladoA = float(input('Digite o Lado A do seu retangulo: '))
        ladoB = float(input('Digite o Lado B do seu retangulo: '))
        print('A área do seu retangulo é: \n', retangulo(ladoA, ladoB))

    if escolha == 2:
        base = float(input('Digite a base do seu triangulo: '))
        altura = float(input('Digite a altura do seu triangulo: '))
        print('A área do seu triangulo é: \n', triangulo(base, altura))


    if escolha == 3:
        raio = float(input('Digite o raio do seu círculo: '))
        print('A área do seu círculo é: \n', circulo(raio))


    if escolha == 4:
       print('\nSaindo do programa...\n')
       break