def encontrarMaisVelhoMaisAlto(nomes, idades, alturas):

    indice_mais_alto = alturas.index(max(alturas))
    indice_mais_velho = idades.index(max(idades))
    
    print("\nResultados:")
    print(f"Aluno mais alto: {nomes[indice_mais_alto]} - Altura: {alturas[indice_mais_alto]:.2f}m - Idade: {idades[indice_mais_alto]} anos")
    print(f"Aluno mais velho: {nomes[indice_mais_velho]} - Altura: {alturas[indice_mais_velho]:.2f}m - Idade: {idades[indice_mais_velho]} anos")

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

encontrarMaisVelhoMaisAlto(nomes, idades, alturas)
