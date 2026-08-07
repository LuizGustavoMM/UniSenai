#Liste os atores que atuaram em todos os programas de comédia.

SELECT a.nome AS Ator FROM ator a
JOIN prog_elenco pe ON a.id_ator = pe.fk_id_ator
JOIN programa_tv p ON pe.fk_id_programa = p.id_programa
JOIN prog_genero pg ON p.id_programa = pg.fk_id_programa
WHERE pe.chk_ator = 1 AND pg.fk_id_genero = 2 
GROUP BY a.id_ator, a.nome HAVING 
    COUNT(DISTINCT p.id_programa) = (
        SELECT COUNT(DISTINCT pg2.fk_id_programa)
        FROM prog_genero pg2
        WHERE pg2.fk_id_genero = 2 
    );
