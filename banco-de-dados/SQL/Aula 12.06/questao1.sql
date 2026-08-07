#Liste os títulos dos programas que possuem ao menos 3 gêneros associados a eles.

SELECT p.titulo AS Programa
FROM programa_tv p
INNER JOIN prog_genero pg ON p.id_programa = pg.fk_id_programa
GROUP BY p.id_programa, p.titulo
HAVING  COUNT(pg.fk_id_genero) >= 3;
