-- Criação do banco de dados
CREATE DATABASE IF NOT EXISTS mercado;
USE mercado;

-- Tabela de produtos
CREATE TABLE IF NOT EXISTS produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    estoque INT NOT NULL DEFAULT 0,
    CONSTRAINT uk_produto_nome UNIQUE (nome)
) ENGINE=InnoDB;

-- Tabela de clientes
CREATE TABLE IF NOT EXISTS cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    tipo ENUM('F', 'J') NOT NULL COMMENT 'F-Física, J-Jurídica',
    cpf_cnpj VARCHAR(20) NOT NULL,
    CONSTRAINT uk_cliente_cpf_cnpj UNIQUE (cpf_cnpj),
    CONSTRAINT uk_cliente_nome UNIQUE (nome)
) ENGINE=InnoDB;

-- Tabela de vendas
CREATE TABLE IF NOT EXISTS venda (
    id INT AUTO_INCREMENT PRIMARY KEY,
    data_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cliente_id INT NULL,
    total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Tabela de itens de venda
CREATE TABLE IF NOT EXISTS item_venda (
    id INT PRIMARY KEY AUTO_INCREMENT,
    venda_id INT NOT NULL,
    produto_id INT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (venda_id) REFERENCES venda(id) ON DELETE CASCADE,
    FOREIGN KEY (produto_id) REFERENCES produto(id),
    CONSTRAINT chk_quantidade CHECK (quantidade > 0)
) ENGINE=InnoDB;