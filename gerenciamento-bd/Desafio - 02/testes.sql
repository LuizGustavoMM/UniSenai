-- PARTE 3: TESTES E VERIFICAÇÃO

USE empresa;

-- Teste 1: Inserção Simples (Deve disparar 'funcionarios_insert')
INSERT INTO funcionarios (id, nome, cargo, salario) 
VALUES (1, 'Maria Silva', 'Gerente', 5000.00);

-- Teste 2: Atualização Simples (Deve disparar 'funcionarios_update')
UPDATE funcionarios 
SET cargo = 'Gerente Sênior', salario = 7000.00 
WHERE id = 1;

-- Teste 3: Inserção Múltipla (Deve disparar 'funcionarios_insert' 2 vezes)
INSERT INTO funcionarios (id, nome, cargo, salario) VALUES
(2, 'Joao Costa', 'Analista de Sistemas', 6200.00),
(3, 'Pedro Alves', 'Desenvolvedor Jr', 3800.00);

-- Teste 4: Atualização Múltipla (Deve disparar 'funcionarios_update' 1 vez)
UPDATE funcionarios 
SET salario = salario + 200 
WHERE cargo = 'Desenvolvedor Jr';

-- Teste 5: Exclusão Simples (Deve disparar 'funcionarios_delete')
DELETE FROM funcionarios 
WHERE id = 2;