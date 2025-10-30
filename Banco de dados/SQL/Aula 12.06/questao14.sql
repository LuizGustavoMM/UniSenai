#Programas dirigidos por pelo menos um estrangeiro e sem nota inferior a 7

SELECT DISTINCT p.titulo
FROM programa_tv p
JOIN prog_elenco pe ON p.id_programa = pe.fk_id_programa
JOIN ator d ON pe.fk_id_ator = d.id_ator
JOIN prog_informacao pi ON p.id_programa = pi.fk_id_programa
JOIN pais p_pais ON pi.fk_id_pais = p_pais.id_pais
JOIN pais d_pais ON d.fk_id_pais = d_pais.id_pais
WHERE pe.chk_diretor = 1
  AND d_pais.id_pais <> p_pais.id_pais -- diretor estrangeiro
  AND p.id_programa NOT IN (
    SELECT av.fk_id_programa
    FROM avaliacao av
    WHERE av.nota < 7
  );
