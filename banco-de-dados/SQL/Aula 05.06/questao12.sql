SELECT p.titulo AS Programa, pais.nome AS Pais, pi.sinopse, p.ano
FROM programa_tv p
INNER JOIN prog_informacao pi ON p.id_programa = pi.fk_id_programa
INNER JOIN pais ON pi.fk_id_pais = pais.id_pais
WHERE p.ano BETWEEN 2000 AND 2010
ORDER BY pais.nome ASC;
