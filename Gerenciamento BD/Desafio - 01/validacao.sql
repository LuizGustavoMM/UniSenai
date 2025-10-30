SHOW CREATE TABLE Clientes;
SHOW CREATE TABLE Pedidos;
SHOW CREATE TABLE ItensPedidos;

DESCRIBE Clientes;
DESCRIBE Funcionarios;

-- Teste 1: Tentar inserir um produto com preço negativo (deve falhar)
INSERT INTO Produtos (nome, descricao, preco, quantidade_estoque) 
VALUES ('Produto Teste', 'Descrição Teste', -50.00, 10);

-- Teste 2: Tentar inserir um pedido com um status inválido (deve falhar)
INSERT INTO Pedidos (id_cliente, id_funcionario, data_pedido, status) 
VALUES (1, 1, CURDATE(), 'Aguardando');

SELECT
    CONSTRAINT_NAME,          
    TABLE_NAME,               
    REFERENCED_TABLE_NAME,    
    DELETE_RULE               
FROM
    INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS
WHERE
    CONSTRAINT_SCHEMA = 'loja';
