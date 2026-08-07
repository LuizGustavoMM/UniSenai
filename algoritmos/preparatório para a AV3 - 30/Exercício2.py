def filtroAltura(nomes, idades, alturas):
    print('Serão admitidas apenas as pessoas com altura igual ou superior a 1.70m.')
    for i, altura in enumerate(alturas):
        if altura >= 1.70:
            print(f'Nome: {nomes[i]} - Altura: {altura}m - Idade: {idades[i]}')

nomes = []
idades = []
alturas = []

while True:
    nome = str(input('Digite um nome ou "sair" para fechar o programa.\nR: ')).strip()
    if nome.upper() == 'SAIR':
        break

    nomes.append(nome)
    idade = int(input('Digite a idade: '))
    idades.append(idade)
    altura = float(input('Digite a altura em metros: '))
    alturas.append(altura)

filtroAltura(nomes, idades, alturas)