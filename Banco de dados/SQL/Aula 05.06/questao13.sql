SELECT p.titulo AS Programa, COUNT(DISTINCT d.fk_id_plataforma) AS Total_Plataformas
FROM programa_tv p
INNER JOIN disponibilidade d ON p.id_programa = d.fk_id_programa
GROUP BY p.id_programa, p.titulo
HAVING COUNT(DISTINCT d.fk_id_plataforma) >= 3;
