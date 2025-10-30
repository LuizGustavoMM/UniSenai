#Atores que atuaram em mais de 3 programas dirigidos por diretores diferentes.

SELECT a.nome AS Ator, COUNT(DISTINCT p.id_programa) AS Total_Programas,
COUNT(DISTINCT pe_diretor.fk_id_ator) AS Diretores_Diferentes FROM ator a
JOIN prog_elenco pe_ator ON a.id_ator = pe_ator.fk_id_ator
JOIN programa_tv p ON pe_ator.fk_id_programa = p.id_programa
JOIN prog_elenco pe_diretor ON p.id_programa = pe_diretor.fk_id_programa AND pe_diretor.chk_diretor = 1
WHERE pe_ator.chk_ator = 1 GROUP BY a.id_ator, a.nome
HAVING COUNT(DISTINCT p.id_programa) > 3 AND COUNT(DISTINCT pe_diretor.fk_id_ator) > 1;
