CREATE TABLE "usuario" (
    "id" bigserial PRIMARY KEY,
    "nome" varchar(255) NOT NULL,
    "email" varchar(255) NOT NULL CONSTRAINT "usuario_email_key" UNIQUE,
    "telefone" varchar(255) NOT NULL,
    "senha_hash" varchar(255) NOT NULL,
    "perfil" varchar(255) NOT NULL,
    "foto_url" text,
    "cidade" varchar(100),
    CONSTRAINT "usuario_perfil_check"
        CHECK ("perfil" IN ('estudante', 'proprietario'))
);

CREATE TABLE "estudante" (
    "id" bigint PRIMARY KEY REFERENCES "usuario"("id"),
    "nome" varchar(255) NOT NULL,
    "email" varchar(255) NOT NULL CONSTRAINT "estudante_email_key" UNIQUE,
    "telefone" varchar(255) NOT NULL,
    "curso" varchar(255) NOT NULL,
    "criado_em" timestamp DEFAULT CURRENT_TIMESTAMP,
    "atualizado_em" timestamp DEFAULT CURRENT_TIMESTAMP,
    "foto_url" varchar(500),
    "sobre_mim" text
);

CREATE TABLE "proprietario" (
    "id" bigint PRIMARY KEY REFERENCES "usuario"("id"),
    "nome" varchar(255) NOT NULL,
    "email" varchar(255) NOT NULL CONSTRAINT "proprietario_email_key" UNIQUE,
    "telefone" varchar(255) NOT NULL,
    "criado_em" timestamp DEFAULT CURRENT_TIMESTAMP,
    "atualizado_em" timestamp DEFAULT CURRENT_TIMESTAMP,
    "foto_url" varchar(500),
    "sobre_mim" text
);

CREATE TABLE "vaga" (
    "id" bigserial PRIMARY KEY,
    "responsavel_id" bigint NOT NULL REFERENCES "usuario"("id"),
    "titulo" varchar(255) NOT NULL,
    "descricao" varchar(255) NOT NULL,
    "endereco" varchar(255) NOT NULL,
    "valor_aluguel" numeric(10, 2) NOT NULL,
    "numero_moradores" integer NOT NULL,
    "regras_casa" varchar(255) NOT NULL,
    "latitude" numeric(10, 8) NOT NULL,
    "longitude" numeric(11, 8) NOT NULL,
    "status" varchar(255) NOT NULL,
    "aceita_barulho_depois_das_22" boolean NOT NULL,
    "fuma" boolean NOT NULL,
    "aceita_animais" boolean NOT NULL,
    "gosta_receber_visitas" boolean NOT NULL,
    "gosta_cozinhar" boolean NOT NULL,
    "estuda_em_casa" boolean NOT NULL,
    "aceita_dividir_tarefas" boolean NOT NULL,
    "aceita_dividir_quarto" boolean NOT NULL,
    "aceita_festas" boolean NOT NULL,
    "comunicativo" boolean NOT NULL,
    "criado_em" timestamp DEFAULT CURRENT_TIMESTAMP,
    "atualizado_em" timestamp DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "vaga_status_check"
        CHECK ("status" IN ('ativa', 'inativa'))
);

CREATE TABLE "favorito" (
    "estudante_id" bigint NOT NULL REFERENCES "estudante"("id"),
    "vaga_id" bigint NOT NULL REFERENCES "vaga"("id"),
    CONSTRAINT "favorito_pkey"
        PRIMARY KEY("estudante_id", "vaga_id")
);

CREATE TABLE "interesse" (
    "id" bigserial PRIMARY KEY,
    "estudante_id" bigint NOT NULL REFERENCES "estudante"("id"),
    "vaga_id" bigint NOT NULL REFERENCES "vaga"("id"),
    "criado_em" timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "status_envio_estudante" varchar(20) DEFAULT 'pendente' NOT NULL,
    "status_envio_proprietario" varchar(20) DEFAULT 'pendente' NOT NULL,
    CONSTRAINT "interesse_estudante_id_vaga_id_key"
        UNIQUE("estudante_id", "vaga_id"),
    CONSTRAINT "interesse_status_envio_estudante_check"
        CHECK ("status_envio_estudante" IN ('pendente', 'enviado', 'falhou')),
    CONSTRAINT "interesse_status_envio_proprietario_check"
        CHECK ("status_envio_proprietario" IN ('pendente', 'enviado', 'falhou'))
);

CREATE TABLE "questionario_estudante" (
    "id" bigserial PRIMARY KEY,
    "estudante_id" bigint NOT NULL
        REFERENCES "estudante"("id")
        CONSTRAINT "questionario_estudante_estudante_id_key" UNIQUE,
    "aceita_barulho_depois_das_22" boolean NOT NULL,
    "fuma" boolean NOT NULL,
    "aceita_animais" boolean NOT NULL,
    "gosta_receber_visitas" boolean NOT NULL,
    "gosta_cozinhar" boolean NOT NULL,
    "estuda_em_casa" boolean NOT NULL,
    "aceita_dividir_tarefas" boolean NOT NULL,
    "aceita_dividir_quarto" boolean NOT NULL,
    "aceita_festas" boolean NOT NULL,
    "comunicativo" boolean NOT NULL,
    "atualizado_em" timestamp DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "vaga_foto" (
    "id" bigserial PRIMARY KEY,
    "vaga_id" bigint NOT NULL REFERENCES "vaga"("id") ON DELETE CASCADE,
    "url" varchar(500) NOT NULL,
    "ordem" integer NOT NULL
);