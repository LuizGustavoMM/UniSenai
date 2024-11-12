def ler_int(mensagem, mensagem_de_erro):
    while True:
        try:
            entrada = int(input(mensagem))
            return entrada
        except ValueError:
            print(mensagem_de_erro)

def somaNum(a,b):
    if a > 1000:
        raise OverflowError
    else:
        return(a+b)