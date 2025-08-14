-- Insere um pesquisador inicial (que terá o ID 1)
INSERT INTO pesquisadores (nome, email, senha) VALUES ('Dr. Ada Lovelace', 'ada.lovelace@vibe.track', 'senha_segura_123');

-- Insere um participante inicial (que terá o ID 1)
INSERT INTO participantes (nome_completo, email, data_nascimento) VALUES ('Charles Babbage', 'charles.babbage@email.com', '1791-12-26');

-- Insere um experimento inicial e o associa ao pesquisador de ID 1
INSERT INTO experimentos (nome, descricao, data_inicio, data_fim, status_experimento, pesquisador_id) VALUES ('Teste Inicial', 'Experimento para testar a API', '2025-08-01', '2025-08-10', 'PLANEJADO', 1);