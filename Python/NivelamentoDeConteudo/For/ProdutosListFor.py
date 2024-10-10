produtos = ['coca','pepsi','guaraná','sprite','fanta']
producao = [15000,12000,13000,5000,250]

tamanho = len(producao)

for i in range(tamanho):
    print('Produção do {} é de {}.' .format(produtos[i], producao[i]))