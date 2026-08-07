SELECT a.nome AS Diretor, COUNT(DISTINCT p.id_programa) AS Total_Programas_Drama
FROM ator a
INNER JOIN prog_elenco pe ON a.id_ator = pe.fk_id_ator
INNER JOIN programa_tv p ON pe.fk_id_programa = p.id_programa
INNER JOIN prog_genero pg ON p.id_programa = pg.fk_id_programa
INNER JOIN genero g ON pg.fk_id_genero = g.id_genero
WHERE pe.chk_diretor = 1 AND g.nome = 'Drama'
GROUP BY a.nome
HAVING COUNT(DISTINCT p.id_programa) > 1;
