SELECT DISTINCT p.titulo AS Programa
FROM programa_tv p
INNER JOIN prog_genero pg ON p.id_programa = pg.fk_id_programa
INNER JOIN genero g ON pg.fk_id_genero = g.id_genero
INNER JOIN disponibilidade d ON p.id_programa = d.fk_id_programa
INNER JOIN plataforma plat ON d.fk_id_plataforma = plat.id_plataforma
WHERE g.nome IN ('Comédia', 'Musical') AND plat.nome = 'Netflix';
