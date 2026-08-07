#Zerando variáveis
lados = 0
comprimento = 0

#Lados para checagem
lados = int(input("O Programa lê apenas 3 poligonos regulares Triângulo, Quadrado e Pentágono.\n\nDigite quantos lados tem seu poligono: "))

#Checagem de lados, se está dentro do nosso range
if lados == 3 or lados == 4 or lados == 5:
    if lados == 3:
        comprimento = float(input("Digite o comprimento do lado em CM para calcular a área ou perimetro: "))
        print("\nTRIÂNGULO\nO valor do perímetro do triângulo é: {}cm" .format(comprimento * lados))
    elif lados == 4:
        comprimento = float(input("Digite o comprimento do lado em CM para calcular a área ou perimetro: "))
        print("\nQUADRADO\nO valor da área do quadrado é: {}cm²" .format(comprimento * comprimento))
    elif lados == 5:
        print("\nPENTÁGONO\n")

#Tratamento de exceções
else:
    print("Digite um número de lados entre 3, 4 ou 5!")        