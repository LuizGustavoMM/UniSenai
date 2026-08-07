def validarNotas(alunos, notas):
    print('Aprovação somente com nota a cima de 7.')
    for i, nota in enumerate(notas):
        if nota >= 7:
            print(f'{alunos[i]} - Nota: {nota}')

alunos = []
notas = []

while True:
    
    aluno = input('Digite o nome do aluno ou sair para encerrar: ')
    if aluno.strip().upper() == 'SAIR':
        break

    alunos.append(aluno)
    nota = float(input('Digite a nota do aluno: '))
    notas.append(nota)

validarNotas(alunos, notas)