#Exiba os programas que possuem média de avaliação superior à média das séries ('Série').

SELECT p.titulo AS Programa, AVG(av.nota) AS Media_Programa FROM programa_tv p
INNER JOIN avaliacao av ON p.id_programa = av.fk_id_programa
GROUP BY p.id_programa, p.titulo
HAVING 
    AVG(av.nota) > (
        SELECT AVG(av2.nota)
        FROM programa_tv p2
        INNER JOIN avaliacao av2 ON p2.id_programa = av2.fk_id_programa
        WHERE p2.fk_id_tipo = 1
);