#Programas com mais de 3 gêneros e avaliação maior que 8.

SELECT p.titulo AS Programa, COUNT(DISTINCT pg.fk_id_genero) AS Total_Generos,
AVG(av.nota) AS Media_Avaliacao FROM programa_tv p
JOIN prog_genero pg ON p.id_programa = pg.fk_id_programa
JOIN avaliacao av ON p.id_programa = av.fk_id_programa
GROUP BY p.id_programa, p.titulo
HAVING COUNT(DISTINCT pg.fk_id_genero) > 3 AND AVG(av.nota) > 8;
