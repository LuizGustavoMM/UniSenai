import random
#declaração da função que cria a lista aleatória
def criaListaAleatória(tamanho):
    lista=[]
    for i in range(tamanho):
        valor=random.randint(1,10)
        lista.append((valor))
    return lista, max(lista), min(lista)
#chamada da função
tam=5
li, maxli, minli=criaListaAleatória(tam)
print (li)
print(maxli)
print(minli)