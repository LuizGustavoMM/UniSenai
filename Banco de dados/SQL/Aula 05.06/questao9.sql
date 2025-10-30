
SELECT p.titulo AS Programa, COUNT(DISTINCT pg.fk_id_genero) AS Total_Generos, AVG(av.nota) AS Media_Nota
FROM programa_tv p
INNER JOIN prog_genero pg ON p.id_programa = pg.fk_id_programa
INNER JOIN avaliacao av ON p.id_programa = av.fk_id_programa
GROUP BY p.id_programa, p.titulo
HAVING COUNT(DISTINCT pg.fk_id_genero) > 1;
