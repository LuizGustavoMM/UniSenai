SELECT p.titulo AS Programa, AVG(av.nota) AS Media_Nota
FROM programa_tv p
INNER JOIN avaliacao av ON p.id_programa = av.fk_id_programa
GROUP BY p.id_programa, p.titulo
HAVING AVG(av.nota) BETWEEN 6 AND 8;
