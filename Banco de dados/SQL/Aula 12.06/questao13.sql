SELECT p.titulo, d.nome AS diretor
FROM prog_elenco pe
JOIN ator d ON pe.fk_id_ator = d.id_ator
JOIN programa_tv p ON pe.fk_id_programa = p.id_programa
WHERE pe.chk_diretor = 1
AND d.id_ator IN (
    SELECT pe.fk_id_ator
    FROM prog_elenco pe
    WHERE pe.chk_diretor = 1
    GROUP BY pe.fk_id_ator
    HAVING COUNT(*) = (
        SELECT MAX(qtd)
        FROM (
            SELECT COUNT(*) AS qtd
            FROM prog_elenco
            WHERE chk_diretor = 1
            GROUP BY fk_id_ator
        ) AS sub
    )
);