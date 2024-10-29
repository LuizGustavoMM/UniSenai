def mensagem(user):
    return('Olá, {}!' .format(user))

nome = input('Digite seu nome: ')
print(mensagem(nome))