numero = 36
limite_inferior = 10
limite_superior = 50
divisor = 6
dentro_intervalo = (numero>=limite_inferior)*(numero<=limite_superior)
multiplo_divisor = (numero%divisor == 0)
impar_par = numero % 36

resultado = dentro_intervalo * multiplo_divisor * "O número está dentro do intervalo e é multiplo do divisor."
print( resultado, f"O resto da divisão deste número é {impar_par}")