def media(alunos):
    lista = sum.lista
    resultado = alunos / lista
    return resultado

notas = []
cont = 0

while True:
    escolha = int(input('Programa de geração de média aritmética.\n\nDigite a opção que deseja:\n1. digitar nota\n2. Gerar média\n3. Sair do programa\nR: '))
    tamanho = len.notas
    soma = sum.notas
    if escolha == 1:
        cont += 1
        nota = float(input('Digite a nota do {}° aluno: '. format(cont)))
        notas.append(nota)
        print(notas)

    if escolha == 2:
        print(media())

    if escolha == 3:
        print('Fechando programa...')
        break    



print(notas)
print(tamanho)
print(soma)