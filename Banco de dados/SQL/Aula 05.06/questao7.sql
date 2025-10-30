SELECT p.titulo AS Programa, MAX(av.nota) AS Nota
FROM avaliacao av
INNER JOIN programa_tv p ON av.fk_id_programa = p.id_programa
GROUP BY p.titulo
HAVING MAX(av.nota) = (SELECT MAX(nota) FROM avaliacao) OR MAX(av.nota) = (SELECT MIN(nota) FROM avaliacao);
