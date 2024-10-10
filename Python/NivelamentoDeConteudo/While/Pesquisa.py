Lista = [15, 7, 27, 39]
# Define uma lista de valores inteiros.
procurado = int(input("Digite o valor a ser procurado: "))
# Solicita ao usuário que digite um valor, converte a entrada para um número inteiro e o armazena na variável `procurado`.
achou = False
# Inicializa a variável `achou` como False, indicando que o valor ainda não foi encontrado.
x = 0
# Inicializa a variável `x`, que será usada como índice para percorrer a lista.
while x < len(Lista):
    # Inicia um loop `while` que percorre a lista enquanto `x` for menor que o tamanho da lista.
    if Lista[x] == procurado:
        # Verifica se o valor no índice atual (`Lista[x]`) é igual ao valor procurado.
        achou = True
        # Se o valor for encontrado, define `achou` como True.
        break
        # Interrompe o loop, já que o valor foi encontrado.
    x += 1
    # Incrementa o valor de `x` para passar ao próximo índice da lista.
if achou:
# Verifica se o valor foi encontrado (se `achou` é True).
    print("%d achado no índice %d" % (procurado, x))
     # Se o valor foi encontrado, imprime a mensagem com o valor e a posição na lista.
else:
# Se `achou` for False, ou seja, se o valor não foi encontrado:
    print("%d não foi encontrado na lista" % procurado)
    # Imprime uma mensagem informando que o valor não foi encontrado na lista