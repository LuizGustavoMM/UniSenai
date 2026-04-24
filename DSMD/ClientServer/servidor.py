import socket

# Endereco e porta onde o servidor vai escutar conexoes.
HOST = '127.0.0.1'
PORT = 5555

# Estado inicial do jogador no centro aproximado da grade 10x10.
pos_x = 5
pos_y = 5

print('Iniciando o servidor...')

# Cria socket TCP/IPv4.
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Associa o socket ao host/porta configurados.
server_socket.bind((HOST, PORT))

# Coloca socket em modo de escuta por conexoes de clientes.
server_socket.listen()

print(f'Servidor escutando na porta {PORT}...')

# Bloqueia ate um cliente conectar.
conn, addr = server_socket.accept()
print(f'Jogador conectado de: {addr}')

# Loop principal de processamento de comandos.
while True:
    # Recebe ate 1024 bytes do cliente.
    data = conn.recv(1024)

    # Quando data vazio, o cliente encerrou conexao.
    if not data:
        print('Cliente desconectou.')
        break

    # Normaliza comando para facilitar comparacoes.
    comando = data.decode('utf-8').upper().strip()
    print(f'Comando recebido: {comando}')

    # Processa movimentos respeitando limites da grade (0..9).
    if comando in ('UP', 'CIMA'):
        if pos_y > 0:
            pos_y -= 1
    elif comando in ('DOWN', 'BAIXO'):
        if pos_y < 9:
            pos_y += 1
    elif comando in ('LEFT', 'ESQUERDA'):
        if pos_x > 0:
            pos_x -= 1
    elif comando in ('RIGHT', 'DIREITA'):
        if pos_x < 9:
            pos_x += 1
    elif comando == 'POS':
        # Nao move; apenas retorna coordenada atual.
        pass
    else:
        # Retorno padronizado de erro para comando invalido.
        resposta = 'ERRO: comando invalido. Use CIMA/BAIXO/ESQUERDA/DIREITA/POS'
        conn.sendall(resposta.encode('utf-8'))
        continue

    # Retorna coordenadas atuais no formato "x,y".
    resposta = f'{pos_x},{pos_y}'
    conn.sendall(resposta.encode('utf-8'))

# Finalizacao explicita dos sockets.
conn.close()
server_socket.close()
