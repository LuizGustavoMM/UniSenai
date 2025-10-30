#Quais programas estão disponíveis exclusivamente em uma única plataforma?

SELECT p.titulo AS Programa, COUNT(d.fk_id_plataforma) 
AS Total_Plataformas FROM programa_tv p
JOIN disponibilidade d ON p.id_programa = d.fk_id_programa
GROUP BY p.id_programa, p.titulo HAVING COUNT(d.fk_id_plataforma) = 1;