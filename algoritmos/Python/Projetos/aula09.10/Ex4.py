palavras = []

for i in range(5):
    palavra = input('Digite uma palavra: ')
    palavras.append(palavra)

for i, palavra in enumerate(palavras):
    print('No índice =',(i),' temos: ', palavras[i])