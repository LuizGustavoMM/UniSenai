def pesquisa(procurado):
    achou = False
    x = 0
    while x < len(Lista):
        if Lista[x] == procurado:
            achou = True
            break
        x += 1
    
    if achou:
        print("Aluno {} achado no índice {} com a nota {:.1f}".format(alunos[x], x, Lista[x]))
    else:
        print("A nota {:.1f} não foi encontrada na lista.".format(procurado))

Lista = [8.0, 7.4, 2.0, 9.0, 8.5, 10, 5.5]
alunos = ['João', 'Fernando', 'Joel', 'Victor', 'Luiz', 'Thomaz', 'Eric']

pesquisa(float(input("Digite a nota a ser procurada: ")))
