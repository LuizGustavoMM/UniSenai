#Quais programas têm avaliação inferior à nota média de todos os programas?

SELECT p.titulo AS Programa, ROUND(AVG(av.nota), 2) AS Media_Programa
FROM programa_tv p
JOIN avaliacao av ON p.id_programa = av.fk_id_programa
GROUP BY p.id_programa, p.titulo
HAVING AVG(av.nota) < (SELECT AVG(nota) FROM avaliacao);
