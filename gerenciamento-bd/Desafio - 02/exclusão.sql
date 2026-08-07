-- Seleciona o banco de dados que queremos usar
USE empresa;

-- Desativa temporariamente a verificação de chaves estrangeiras
-- (Boa prática ao truncar múltiplas tabelas que podem se relacionar)
SET FOREIGN_KEY_CHECKS = 0; 

-- Limpa todos os dados da tabela de auditoria
TRUNCATE TABLE auditoria_funcionarios;

-- Limpa todos os dados da tabela de funcionários
TRUNCATE TABLE funcionarios;

-- Reativa a verificação de chaves estrangeiras
SET FOREIGN_KEY_CHECKS = 1; 

-- Confirma que as tabelas estão vazias
SELECT * FROM funcionarios;
SELECT * FROM auditoria_funcionarios;