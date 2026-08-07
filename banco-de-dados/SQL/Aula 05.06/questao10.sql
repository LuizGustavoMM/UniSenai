SELECT a.nome AS Ator, COUNT(pe.fk_id_programa) AS Total_Programas
FROM ator a
INNER JOIN prog_elenco pe ON a.id_ator = pe.fk_id_ator
WHERE pe.chk_ator = 1
GROUP BY a.id_ator, a.nome
ORDER BY Total_Programas DESC
LIMIT 5;
