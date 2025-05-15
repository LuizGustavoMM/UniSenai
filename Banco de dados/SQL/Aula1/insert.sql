-- Inserir em tipo
INSERT INTO tipo (nome) VALUES ('Série'), ('Filme');

-- Inserir em pais
INSERT INTO pais (nome) VALUES ('Brasil'), ('EUA');

-- Inserir em ator
INSERT INTO ator (nome, fk_id_pais) VALUES ('Wagner Moura', 1), ('Tom Hanks', 2);

-- Inserir em contatos
INSERT INTO contatos (nome, email, senha) VALUES ('Ana', 'ana@email.com', 'senha123'), ('Carlos', 'carlos@email.com', 'abc123');

-- Inserir em genero
INSERT INTO genero (nome) VALUES ('Drama'), ('Ação');

-- Inserir em plataforma
INSERT INTO plataforma (nome) VALUES ('Netflix'), ('HBO Max');

-- Inserir em programatv
INSERT INTO programatv (titulo, ano, fk_id_tipo) VALUES ('Tropa de Elite', 2007, 2), ('Forrest Gump', 1994, 2);

-- Inserir em prog_informacoes
INSERT INTO prog_informacoes (fk_id_programa, titulo_original, sinopse)
VALUES 
(1, 'Elite Squad', 'Um capitão do BOPE lida com o tráfico e corrupção no Rio.'),
(2, 'Forrest Gump', 'Um homem simples vive eventos marcantes da história dos EUA.');

-- Inserir em prog_elenco
INSERT INTO prog_elenco (fk_id_programa, fk_id_ator, chk_ator, chk_diretor)
VALUES 
(1, 1, true, false),
(2, 2, true, false);

-- Inserir em disponibilidade
INSERT INTO disponibilidade (fk_id_programa, fk_id_plataforma)
VALUES (1, 1), (2, 2);

-- Inserir em prog_genero
INSERT INTO prog_genero (id_programa, id_genero)
VALUES (1, 2), (2, 1);

-- Inserir em avaliacao
INSERT INTO avaliacao (fk_id_contato, fk_id_programa, nota)
VALUES (1, 1, 9.0), (2, 2, 8.5);