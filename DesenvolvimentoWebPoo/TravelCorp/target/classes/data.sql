DELETE FROM viagem;

INSERT INTO viagem (colaborador, destino, data_inicio, data_retorno, motivo, status) VALUES
('Ana Souza', 'São Paulo - SP', '2026-07-01', '2026-07-05', 'Reunião com cliente corporativo', 'PLANEJADA'),
('Carlos Pereira', 'Rio de Janeiro - RJ', '2026-06-20', '2026-06-25', 'Visita técnica à filial', 'EM_ANDAMENTO'),
('Mariana Lima', 'Belo Horizonte - MG', '2026-05-10', '2026-05-14', 'Treinamento de equipe comercial', 'CONCLUIDA'),
('João Ferreira', 'Curitiba - PR', '2026-08-12', '2026-08-15', 'Participação em feira do setor', 'PLANEJADA'),
('Beatriz Almeida', 'Porto Alegre - RS', '2026-06-18', '2026-06-22', 'Auditoria interna', 'EM_ANDAMENTO'),
('Rafael Gomes', 'Recife - PE', '2026-04-02', '2026-04-06', 'Implantação de novo sistema', 'CONCLUIDA');
