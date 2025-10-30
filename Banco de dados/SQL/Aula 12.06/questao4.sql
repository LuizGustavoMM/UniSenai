#Quais programas foram dirigidos por atores que também atuaram em outros programas?

SELECT DISTINCT p1.titulo AS Programa FROM programa_tv p1
JOIN prog_elenco pe1 ON p1.id_programa = pe1.fk_id_programa WHERE 
    pe1.chk_diretor = 1
    AND pe1.fk_id_ator IN (
        SELECT pe2.fk_id_ator
        FROM prog_elenco pe2
        WHERE pe2.chk_ator = 1
          AND pe2.fk_id_programa <> pe1.fk_id_programa
    );
