SELECT p.titulo AS Programa, COUNT(pe.fk_id_ator) AS Total_Atores
FROM programa_tv p
INNER JOIN prog_elenco pe ON p.id_programa = pe.fk_id_programa
WHERE pe.chk_ator = 1
GROUP BY p.id_programa, p.titulo
HAVING COUNT(pe.fk_id_ator) > 2;
