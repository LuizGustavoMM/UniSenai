-- Seleciona o banco de dados para a sessão
USE loja;

-- Remove as chaves estrangeiras existentes para permitir a modificação das chaves primárias
ALTER TABLE Pedidos DROP FOREIGN KEY pedidos_ibfk_1;
ALTER TABLE Pedidos DROP FOREIGN KEY pedidos_ibfk_2;
ALTER TABLE ItensPedidos DROP FOREIGN KEY itenspedidos_ibfk_1;
ALTER TABLE ItensPedidos DROP FOREIGN KEY itenspedidos_ibfk_2;

-- Adiciona a propriedade AUTO_INCREMENT às chaves primárias
ALTER TABLE Clientes MODIFY COLUMN id INT AUTO_INCREMENT;
ALTER TABLE Funcionarios MODIFY COLUMN id INT AUTO_INCREMENT;
ALTER TABLE Produtos MODIFY COLUMN id INT AUTO_INCREMENT;
ALTER TABLE Pedidos MODIFY COLUMN id INT AUTO_INCREMENT;

-- Adiciona constraints de verificação (CHECK) para garantir a integridade dos dados
ALTER TABLE Produtos
    ADD CONSTRAINT chk_preco_positivo CHECK (preco > 0),
    ADD CONSTRAINT chk_estoque_nao_negativo CHECK (quantidade_estoque >= 0);

ALTER TABLE Funcionarios
    ADD CONSTRAINT chk_salario_positivo CHECK (salario > 0);

ALTER TABLE ItensPedidos
    ADD CONSTRAINT chk_quantidade_positiva CHECK (quantidade > 0);

ALTER TABLE Pedidos
    ADD CONSTRAINT chk_status_pedido CHECK (status IN ('Em andamento', 'Enviado', 'Entregue', 'Cancelado'));

-- Ajusta a coluna 'senha' para ter o tamanho adequado para armazenar hashes
ALTER TABLE Clientes MODIFY COLUMN senha VARCHAR(255) NOT NULL;

-- Recria as chaves estrangeiras com as políticas ON DELETE corretas
ALTER TABLE Pedidos
    ADD CONSTRAINT fk_pedidos_cliente FOREIGN KEY (id_cliente) REFERENCES Clientes(id) ON DELETE SET NULL,
    ADD CONSTRAINT fk_pedidos_funcionario FOREIGN KEY (id_funcionario) REFERENCES Funcionarios(id) ON DELETE SET NULL;

ALTER TABLE ItensPedidos
    ADD CONSTRAINT fk_itens_pedido FOREIGN KEY (id_pedido) REFERENCES Pedidos(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_itens_produto FOREIGN KEY (id_produto) REFERENCES Produtos(id) ON DELETE RESTRICT;