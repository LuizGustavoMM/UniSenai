CREATE DATABASE loja;
USE loja;

-- Criação da tabela Clientes
CREATE TABLE Clientes (
    id INT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL, 
    endereco VARCHAR(255) NOT NULL,
    telefone VARCHAR(15)
);

-- Criação da tabela Funcionários
CREATE TABLE Funcionarios (
    id INT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cargo VARCHAR(255) NOT NULL,
    salario DECIMAL(10, 2) NOT NULL,
    data_contratacao DATE NOT NULL
);

-- Criação da tabela Produtos
CREATE TABLE Produtos (
    id INT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10, 2) NOT NULL,
    quantidade_estoque INT NOT NULL
);

-- Criação da tabela Pedidos
CREATE TABLE Pedidos (
    id INT PRIMARY KEY,
    id_cliente INT,
    id_funcionario INT,
    data_pedido DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES Clientes (id),
    FOREIGN KEY (id_funcionario) REFERENCES Funcionarios (id)
);

-- Criação da tabela Itens de Pedidos
CREATE TABLE ItensPedidos (
    id_pedido INT,
    id_produto INT,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id_pedido, id_produto),
    FOREIGN KEY (id_pedido) REFERENCES Pedidos (id),
    FOREIGN KEY (id_produto) REFERENCES Produtos (id)
);

-- Populando as tabelas
INSERT INTO Clientes (id, nome, email, senha, endereco, telefone) VALUES
(1, 'Maria Silva', 'maria@gmail.com', 'senha123', 'Rua das Flores, 15', '11987654321'),
(2, 'João Souza', 'joao@hotmail.com', 'joao123', 'Av. Paulista, 200', '11923456789');

INSERT INTO Funcionarios (id, nome, cargo, salario, data_contratacao) VALUES
(1, 'Ana Cardoso', 'Gerente', 4500.00, '2022-01-15'),
(2, 'Carlos Menezes', 'Vendedor', 2500.00, '2021-10-01');

INSERT INTO Produtos (id, nome, descricao, preco, quantidade_estoque) VALUES
(1, 'Smartphone XYZ', 'Smartphone com tela de 6.5", 128GB de armazenamento e câmera de 48MP', 1500.00, 25),
(2, 'Fone de Ouvido Bluetooth', 'Fone de ouvido bluetooth com cancelamento de ruído', 300.00, 50);

INSERT INTO Pedidos (id, id_cliente, id_funcionario, data_pedido, status) VALUES
(1, 1, 1, '2023-03-25', 'Entregue'),
(2, 2, 2, '2023-03-28', 'Em andamento');

INSERT INTO ItensPedidos (id_pedido, id_produto, quantidade, preco_unitario) VALUES
(1, 1, 1, 1500.00),
(1, 2, 2, 300.00),
(2, 2, 1, 300.00);