#Plataformas que disponibilizam todos os programas do gênero "Infantil".

SELECT pf.nome AS Plataforma
FROM plataforma pf
JOIN disponibilidade d ON pf.id_plataforma = d.fk_id_plataforma
JOIN prog_genero pg ON d.fk_id_programa = pg.fk_id_programa
WHERE pg.fk_id_genero = 18 
GROUP BY pf.id_plataforma, pf.nome
HAVING 
    COUNT(DISTINCT d.fk_id_programa) = (
        SELECT COUNT(DISTINCT pg2.fk_id_programa)
        FROM prog_genero pg2
        WHERE pg2.fk_id_genero = 18
    );
