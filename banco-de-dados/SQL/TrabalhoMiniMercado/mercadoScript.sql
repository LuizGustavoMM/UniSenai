-- Criação do banco de dados
CREATE DATABASE IF NOT EXISTS mercado;
USE mercado;

-- Tabela de clientes
CREATE TABLE clientes (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cpf CHAR(11) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(12),
    categoria ENUM('SEM_FIDELIDADE', 'FIDELIZADO') NOT NULL,
    INDEX idx_cpf (cpf)
);


-- Tabela de produtos (com ID como chave primária)
CREATE TABLE IF NOT EXISTS estoque (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    preco DECIMAL(10,2) NOT NULL,
    quantidade INT NOT NULL
);

-- Tabela de vendas
CREATE TABLE IF NOT EXISTS vendas (
	id INT AUTO_INCREMENT PRIMARY KEY,
	data_hora DATETIME NOT NULL,
	cliente_id INT NOT NULL,
	desconto DECIMAL(10,2) NOT NULL,
	valor_total DECIMAL(10,2) NOT NULL,
	FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

-- Tabela de itens da venda (com referência ao ID do produto)
CREATE TABLE IF NOT EXISTS itens_venda (
    venda_id INT,
    estoque_id INT,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (venda_id, estoque_id),
    FOREIGN KEY (venda_id) REFERENCES vendas(id),
    FOREIGN KEY (estoque_id) REFERENCES estoque(id)
);