estoque = ['Amoxilina', 'Morfina', 'Setralina', 'Depakot']
precos = [12.99, 20.99, 80.99, 120.99]
tamanho_lista = len(estoque)

while True:
    remedio_desejado = input('Digite o remédio que deseja: ')
    remedio_desejado = remedio_desejado.strip()
    if(remedio_desejado.upper() in estoque):
        indice = estoque.index(remedio_desejado.pper())
        print('')