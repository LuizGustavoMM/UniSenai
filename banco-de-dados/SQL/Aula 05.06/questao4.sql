SELECT DISTINCT p.titulo AS Programa
FROM programa_tv p
INNER JOIN prog_elenco pe ON p.id_programa = pe.fk_id_programa
INNER JOIN avaliacao av ON p.id_programa = av.fk_id_programa;
