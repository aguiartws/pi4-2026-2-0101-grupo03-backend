INSERT INTO usuario (nome, email, telefone, senha_hash, perfil) VALUES
  ('Maria Silva',    'maria@finduhouse.com',   '19999990001', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'proprietario'),
  ('João Souza',     'joao@finduhouse.com',    '19999990002', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'proprietario'),
  ('Ana Oliveira',   'ana@finduhouse.com',     '19999990003', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'proprietario'),
  ('Carlos Pereira', 'carlos@finduhouse.com',  '19999990004', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'proprietario'),
  ('Beatriz Lima',   'bia@finduhouse.com',     '19999990005', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'proprietario')
ON CONFLICT (email) DO NOTHING;