SELECT plat.nome AS Plataforma, COUNT(DISTINCT d.fk_id_programa) AS Total_Programas
FROM disponibilidade d
INNER JOIN plataforma plat ON d.fk_id_plataforma = plat.id_plataforma
GROUP BY plat.nome
HAVING COUNT(DISTINCT d.fk_id_programa) > 5;
