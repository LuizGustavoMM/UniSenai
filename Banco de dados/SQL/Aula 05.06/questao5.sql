SELECT p.titulo AS Programa, SUM(av.nota) AS Total_Notas
FROM programa_tv p
INNER JOIN avaliacao av ON p.id_programa = av.fk_id_programa
GROUP BY p.id_programa, p.titulo
ORDER BY Total_Notas DESC;
