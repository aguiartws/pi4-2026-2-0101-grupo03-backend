CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    perfil VARCHAR(50) NOT NULL CHECK (perfil IN ('estudante', 'proprietario'))
);

CREATE TABLE estudante (
    id INT PRIMARY KEY REFERENCES usuario(id),
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    curso VARCHAR(255) NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE proprietario (
    id INT PRIMARY KEY REFERENCES usuario(id),
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE vaga (
    id SERIAL PRIMARY KEY,
    responsavel_id INT NOT NULL REFERENCES usuario(id),
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    valor_aluguel DECIMAL(10,2) NOT NULL,
    numero_moradores INT NOT NULL,
    regras_casa TEXT NOT NULL,
    latitude DECIMAL(10,8) NOT NULL,
    longitude DECIMAL(11,8) NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('ativa', 'em_analise', 'inativa')),

    horario_dormir_semana VARCHAR(50) NOT NULL,
    horario_dormir_fim_semana VARCHAR(50) NOT NULL,
    horario_acordar_semana VARCHAR(50) NOT NULL,
    horario_acordar_fim_semana VARCHAR(50) NOT NULL,
    nivel_limpeza VARCHAR(50) NOT NULL,
    nivel_organizacao VARCHAR(50) NOT NULL,
    tolerancia_ruido VARCHAR(50) NOT NULL,
    frequencia_visitas VARCHAR(50) NOT NULL,
    frequencia_festas VARCHAR(50) NOT NULL,
    tem_pet BOOLEAN NOT NULL,

    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE favorito (
    estudante_id INT NOT NULL REFERENCES estudante(id),
    vaga_id INT NOT NULL REFERENCES vaga(id),
    PRIMARY KEY (estudante_id, vaga_id)
);

CREATE TABLE questionario_estudante (
    id SERIAL PRIMARY KEY,
    estudante_id INT NOT NULL UNIQUE REFERENCES estudante(id),
    horario_dormir_semana VARCHAR(50) NOT NULL,
    horario_dormir_fim_semana VARCHAR(50) NOT NULL,
    horario_acordar_semana VARCHAR(50) NOT NULL,
    horario_acordar_fim_semana VARCHAR(50) NOT NULL,
    nivel_limpeza VARCHAR(50) NOT NULL,
    nivel_organizacao VARCHAR(50) NOT NULL,
    tolerancia_ruido VARCHAR(50) NOT NULL,
    frequencia_visitas VARCHAR(50) NOT NULL,
    frequencia_festas VARCHAR(50) NOT NULL,
    tem_pet BOOLEAN NOT NULL,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE denuncia (
    id SERIAL PRIMARY KEY,
    vaga_id INT NOT NULL REFERENCES vaga(id),
    estudante_id INT NOT NULL REFERENCES estudante(id),
    motivo VARCHAR(100) NOT NULL,
    descricao TEXT NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('pendente', 'em_analise', 'revisada')),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);