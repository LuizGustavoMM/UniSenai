#Quais programas possuem atores de ao menos 3 países diferentes?

SELECT p.titulo AS Programa,
COUNT(DISTINCT a.fk_id_pais) AS Total_Paises
FROM programa_tv p
JOIN prog_elenco pe ON p.id_programa = pe.fk_id_programa
JOIN ator a ON pe.fk_id_ator = a.id_ator
WHERE pe.chk_ator = 1
GROUP BY p.id_programa, p.titulo
HAVING COUNT(DISTINCT a.fk_id_pais) >= 3;
