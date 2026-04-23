import socket

def escuta_alertas():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind(('localhost', 6000))
        s.listen()
        print("Aguardando alertas...")
        while True:
            conn, addr = s.accept()
            print(f"{conn.recv(1024).decode()}")


if __name__=="__main__":
    escuta_alertas()