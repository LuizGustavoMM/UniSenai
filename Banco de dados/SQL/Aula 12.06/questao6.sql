#Qual o país com maior média de notas dos seus programas?

SELECT pa.nome AS Pais, ROUND(AVG(av.nota), 2) AS Media_Notas FROM avaliacao av
JOIN programa_tv p ON av.fk_id_programa = p.id_programa
JOIN prog_informacao pi ON p.id_programa = pi.fk_id_programa
JOIN pais pa ON pi.fk_id_pais = pa.id_pais
GROUP BY pa.id_pais, pa.nome
ORDER BY Media_Notas DESC LIMIT 1;
