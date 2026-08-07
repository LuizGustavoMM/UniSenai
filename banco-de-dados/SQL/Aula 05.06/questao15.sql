SELECT pais.nome AS Pais, SUM(av.nota) AS Total_Notas
FROM avaliacao av
INNER JOIN programa_tv p ON av.fk_id_programa = p.id_programa
INNER JOIN prog_informacao pi ON p.id_programa = pi.fk_id_programa
INNER JOIN pais ON pi.fk_id_pais = pais.id_pais
GROUP BY pais.nome
ORDER BY Total_Notas DESC;
