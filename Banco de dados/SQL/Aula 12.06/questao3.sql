#Quais programas têm sinopses com mais de 20 palavras e possuem gênero "Drama"?

SELECT pt.titulo FROM programa_tv pt
JOIN prog_informacao pi ON pt.id_programa = pi.fk_id_programa
JOIN prog_genero pg ON pt.id_programa = pg.fk_id_programa
JOIN genero g ON pg.fk_id_genero = g.id_genero
WHERE g.nome = 'Drama'
  AND (LENGTH(pi.sinopse) - LENGTH(REPLACE(pi.sinopse, ' ', '')) + 1) > 20;