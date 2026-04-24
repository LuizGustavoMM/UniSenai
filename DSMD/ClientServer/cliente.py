import os
import socket

# Endereco e porta do servidor.
HOST = '127.0.0.1'
PORT = 5555


def clear_screen():
    os.system('cls' if os.name == 'nt' else 'clear')

def draw_grid(pos_x, pos_y):

    clear_screen()
    print('Grade 10x10 (P = jogador)')
    print('Digite: CIMA, BAIXO, ESQUERDA, DIREITA, POS ou SAIR\n')

    # Renderiza linha por linha da grade.
    for y in range(10):
        line = []
        for x in range(10):
            line.append('P' if x == pos_x and y == pos_y else '.')
        print(' '.join(line))


# Cria socket TCP/IPv4 e conecta no servidor.
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect((HOST, PORT))

# Solicita posicao inicial para desenhar a primeira grade.
client_socket.sendall('POS'.encode('utf-8'))
resposta = client_socket.recv(1024).decode('utf-8').strip()

if resposta.startswith('ERRO:'):
    print(resposta)
else:
    x_str, y_str = resposta.split(',')
    draw_grid(int(x_str), int(y_str))

# Loop principal de interacao com o jogador.
while True:
    comando = input('\nComando: ').strip().upper()
    if comando == 'SAIR':
        break

    # Envia comando digitado para o servidor.
    client_socket.sendall(comando.encode('utf-8'))

    # Recebe resposta com nova posicao ou erro.
    resposta = client_socket.recv(1024).decode('utf-8').strip()

    if resposta.startswith('ERRO:'):
        print(resposta)
        continue

    # Espera formato "x,y" para atualizar grade.
    try:
        x_str, y_str = resposta.split(',')
        draw_grid(int(x_str), int(y_str))
    except ValueError:
        print(f'Resposta inesperada do servidor: {resposta}')

# Fecha conexao ao sair do jogo.
client_socket.close()
