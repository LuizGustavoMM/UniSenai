-- IMPLEMENTAÇÃO DOS TRIGGERS
DELIMITER //
-- Trigger 1: Auditoria de INSERÇÕES (funcionarios_insert)

CREATE TRIGGER funcionarios_insert
AFTER INSERT ON funcionarios
FOR EACH ROW
BEGIN
    INSERT INTO auditoria_funcionarios 
        (operacao, data_operacao, id, nome, cargo, salario)
    VALUES 
        ('I', NOW(), NEW.id, NEW.nome, NEW.cargo, NEW.salario);
END;
//

-- Trigger 2: Auditoria de ATUALIZAÇÕES (funcionarios_update)

CREATE TRIGGER funcionarios_update
AFTER UPDATE ON funcionarios
FOR EACH ROW
BEGIN
    INSERT INTO auditoria_funcionarios 
        (operacao, data_operacao, id, nome, cargo, salario)
    VALUES 
        ('U', NOW(), NEW.id, NEW.nome, NEW.cargo, NEW.salario);
END;
//

-- Trigger 3: Auditoria de EXCLUSÕES (funcionarios_delete)

CREATE TRIGGER funcionarios_delete
AFTER DELETE ON funcionarios
FOR EACH ROW
BEGIN
    INSERT INTO auditoria_funcionarios 
        (operacao, data_operacao, id, nome, cargo, salario)
    VALUES 
        ('D', NOW(), OLD.id, OLD.nome, OLD.cargo, OLD.salario);
END;
//
DELIMITER ;