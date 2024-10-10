Valores = [9, 8, 7, 12, 0, 13, 21]
# Define uma lista chamada 'Valores' com os números 9, 8, 7, 12, 0, 13, e 21.
Pares = []
# Cria uma lista vazia que irá armazenar os números pares.
Impares = []
# Cria uma lista vazia que irá armazenar os números ímpares.
for elementos in Valores:
# Inicia um loop que percorre cada elemento da lista 'Valores'.
    if elementos % 2 == 0:
        # Verifica se o elemento atual é par (o resto da divisão por 2 é igual a zero).
        Pares.append(elementos)
        # Se for par, adiciona o elemento à lista 'Pares'.
    else:
    # Caso contrário, o número é ímpar.
        Impares.append(elementos)
        # Adiciona o elemento à lista 'Impares'.
print("Pares: ", Pares)
# Imprime a lista de números pares.
print("Ímpares: ", Impares)
# Imprime a lista de números ímpares.
