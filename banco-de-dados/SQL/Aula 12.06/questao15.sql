#Programas com mais de 5 avaliações distintas e média acima de 7.

SELECT p.titulo,
       COUNT(*) AS total_avaliacoes,
       AVG(av.nota) AS media_notas
FROM programa_tv p
JOIN avaliacao av ON p.id_programa = av.fk_id_programa
GROUP BY p.id_programa, p.titulo
HAVING COUNT(*) > 5
   AND AVG(av.nota) > 7;
