use tv_filmes;
SELECT p.titulo AS Programa, t.nome AS Tipo, p.ano
FROM programa_tv p
INNER JOIN tipo t ON p.fk_id_tipo = t.id_tipo
WHERE t.nome IN ('Série', 'Anime', 'Reality Show')
ORDER BY p.ano DESC;
