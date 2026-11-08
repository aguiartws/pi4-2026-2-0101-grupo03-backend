# findUhouse

Plataforma web que conecta estudantes universitários a moradias compatíveis — repúblicas, kitnets e apartamentos compartilhados — com base em perfil, hábitos e orçamento, substituindo a busca manual em grupos de WhatsApp e redes sociais.

## Integrantes
- Ana Carolina Ferreira Alves Barcaro — @carolbarcaro
- Ana Clara Coelho Chaves  — @anachaves26
- Brenda Maia Bergamasco — @bMaia07
- Caio Cantarin Marques — @caio0409
- Luis Filippi Aguiar da Rocha — @aguiartws
- Maria Júlia Hofstetter Trevisan Pereira — @m7julia

## Documentação
- Visão geral e fluxos (repositório do frontend): https://github.com/aguiartws/pi4-2026-2-0101-grupo03-frontend/tree/main/docs/fluxos
- Contratos: pasta `docs/contratos` deste repositório

---

## 📋 Pré-requisitos

Para subir o projeto numa máquina limpa, instale:

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (inclui `docker` e `docker-compose`)
- [Git](https://git-scm.com/downloads)

> Não é necessário instalar Java, Maven ou PostgreSQL na máquina — tudo roda em containers.

Para verificar:

```bash
docker --version
docker-compose --version
git --version
```

---

## 🚀 1. Subir o projeto do zero (ou após desligar o PC)

Clone o repositório e entre na pasta raiz:

```bash
git clone https://github.com/aguiartws/pi4-2026-2-0101-grupo03-backend.git
cd pi4-2026-2-0101-grupo03-backend
```

Suba os containers (banco, servidor de e-mail e backend) em segundo plano:

```bash
docker-compose up -d
```

**O que faz:** sobe o **Banco de Dados**, o **Servidor de E-mail** e o **Backend** em segundo plano.

Aguarde ~30s na primeira execução (build das imagens + criação do banco via `init.sql`). Verifique:

```bash
docker ps
```

Saída esperada:

```
CONTAINER ID   IMAGE                                                ...   PORTS                      NAMES
xxxxxxxxxxxx   pi4-2026-2-0101-grupo03-backend-backend              ...   0.0.0.0:8081->8080/tcp     finduhouse-server
xxxxxxxxxxxx   pi4-2026-2-0101-grupo03-backend-servidor-email       ...   0.0.0.0:2525->25/tcp       finduhouse-servidor-email
xxxxxxxxxxxx   postgres:16-alpine                                   ...   0.0.0.0:5432->5432/tcp     finduhouse-database
```

A API estará disponível em: **http://localhost:8081**

---

## 🛠️ 2. Atualizar o código (quando você mudar o Java no VS Code)

Sempre que você alterar qualquer arquivo `.java` (Controller, Service, Entity, etc.) ou o `pom.xml`, você precisa **recompilar o backend**:

```bash
docker-compose up -d --build backend
```

**O que faz:** recompila a imagem do backend com o seu código novo e reinicia o container.

> ⚠️ Alterações em `application.properties` também exigem rebuild, pois o arquivo é empacotado dentro do JAR.

---

## 📜 3. Ver os logs (para saber o que está acontecendo)

### Ver os e-mails chegando no servidor de teste

```bash
docker logs -f finduhouse-servidor-email
```

Aperte **`Ctrl + C`** para sair do modo de leitura.

A cada 10s o servidor imprime o total de e-mails recebidos e, para cada um, **assunto**, **remetente**, **destinatários** e **corpo**. Exemplo:

```
Total de emails recebidos: 2
Assunto: Interesse registrado na vaga: Quarto perto da PUC
Remetente: nao-responda@finduhouse.com
Destinatários: [maria.estudante@puc-campinas.edu.br]
Conteúdo:
Olá Maria Estudante,
...
----------------------
```

### Ver os logs do backend (erros ou inicialização)

```bash
docker logs finduhouse-server
```

> 💡 Use `docker logs finduhouse-server -f` para acompanhar em tempo real.

---

## 🛑 4. Parar tudo (quando terminar de trabalhar)

```bash
docker-compose down
```

**O que faz:** para e remove os containers. **Não apaga os dados do banco** (o volume é preservado).

Para subir de novo depois: `docker-compose up -d`.

---

## 💥 5. Limpar tudo e começar do zero (CUIDADO!)

Se você precisar recriar o banco de dados (por exemplo, após mudanças no `init.sql` ou erro de schema) e **apagar todos os dados de teste**:

```bash
docker-compose down -v
docker-compose up -d --build
```

**O que faz:** o `-v` **apaga o volume do banco de dados**. Depois, sobe tudo de novo rodando o `init.sql` do zero.

> ⚠️ **Todos os dados** (usuários, vagas, interesses) serão **perdidos**. Use com cuidado.

---

## 🧹 6. Limpar cache do Docker (se o código não atualizar de jeito nenhum)

Se você alterar o código e o Docker insistir em rodar a versão antiga (cache de camadas):

```bash
docker-compose down
docker builder prune -f
docker rmi pi4-2026-2-0101-grupo03-backend-backend
docker-compose up -d --build
```

**O que faz:** limpa o cache de build do Docker, remove a imagem antiga do backend e rebuilda do zero.

> 💡 Se persistir, adicione `--no-cache` ao build:
> ```bash
> docker-compose build --no-cache backend
> docker-compose up -d
> ```

---

## 👤 7. Usuários de exemplo

O banco é populado automaticamente pelo `init.sql` na primeira subida (ou após `docker-compose down -v`). Usuários disponíveis:

| Perfil | Nome | E-mail | Telefone (wa.me) |
|--------|------|--------|------------------|
| Estudante | Maria Estudante | `maria.estudante@puc-campinas.edu.br` | `11999999999` |
| Proprietário | Carlos Proprietario | `carlos.proprietario@puc-campinas.edu.br` | `5511999999999` |

> ⚠️ **Senhas:** o `init.sql` armazena apenas o `senha_hash` (BCrypt). Para testar o login, use o **cadastro** (`POST /api/usuarios` ou similar) ou consulte o grupo para a senha em texto dos usuários de exemplo.
>
> 💡 **Telefone e `wa.me`:** o link `wa.me` espera o telefone no formato **E.164** (`55` + DDD + número, sem espaços). O telefone da Maria no `init.sql` **não tem o `55`** — se for testar o link, ajuste no banco.

### Criar um usuário proprietário manualmente (para testar o H11.4)

```bash
# 1) criar o usuário (perfil em minúsculas, conforme check constraint)
docker exec -it finduhouse-database psql -U finduhouse -d finduhouse -c \
  "INSERT INTO usuario (nome, email, telefone, senha_hash, perfil) VALUES ('Carlos Proprietario', 'carlos.proprietario@puc-campinas.edu.br', '5511999999999', 'senha123', 'proprietario') RETURNING id;"

# 2) criar o registro em proprietario (mesmo id retornado)
docker exec -it finduhouse-database psql -U finduhouse -d finduhouse -c \
  "INSERT INTO proprietario (id, nome, email, telefone, criado_em, atualizado_em) VALUES (<ID>, 'Carlos Proprietario', 'carlos.proprietario@puc-campinas.edu.br', '5511999999999', NOW(), NOW());"

# 3) criar uma vaga (status em minúsculas: 'ativa', 'em_analise', 'inativa')
docker exec -it finduhouse-database psql -U finduhouse -d finduhouse -c \
  "INSERT INTO vaga (responsavel_id, titulo, descricao, endereco, valor_aluguel, numero_moradores, regras_casa, latitude, longitude, status, aceita_barulho_depois_das_22, fuma, aceita_animais, gosta_receber_visitas, gosta_cozinhar, estuda_em_casa, aceita_dividir_tarefas, aceita_dividir_quarto, aceita_festas, comunicativo) VALUES (<ID>, 'Quarto perto da PUC', 'Quarto individual, 12m²', 'Rua das Flores, 123', 1200.00, 2, 'Sem festas', -22.9056, -47.0608, 'ativa', true, false, true, true, true, true, true, false, false, true) RETURNING id;"
```

---

## 📧 8. Provedor de e-mail

O backend usa **Spring Mail** (`JavaMailSender`), configurado em `backend/src/main/resources/application.properties`:

```properties
spring.mail.host=finduhouse-servidor-email
spring.mail.port=25
spring.mail.properties.mail.smtp.auth=false
spring.mail.properties.mail.smtp.starttls.enable=false
```

- **Remetente padrão dos e-mails:** `nao-responda@finduhouse.com`
- **Autenticação SMTP:** desabilitada (servidor de teste)
- **STARTTLS:** desabilitado (servidor de teste)

> ⚠️ O host é o **nome do serviço no Docker Compose** (`finduhouse-servidor-email`), não `localhost`. Dentro da rede Docker, `localhost` seria o próprio backend. A porta é a **interna** do container (`25`).

Para trocar para um provedor real (ex.: Gmail, SendGrid), altere o `application.properties` com as credenciais do provedor e rebuild:

```bash
docker-compose up -d --build backend
```

---

## 🧪 9. Servidor de e-mail de teste

O **servidor de e-mail de teste** é o [GreenMail](https://greenmail-mail-test.github.io/greenmail/) rodando em `servidor-email/`:

- **SMTP:** porta `25` (interna no container) / `2525` (exposta no host)
- **Mensagens:** ficam **em memória** (não persistem ao reiniciar o container)
- **Logs:** a cada 10s imprime o total de e-mails e o conteúdo de cada um

### Como testar o envio de dois e-mails (H11.4)

Com os containers no ar:

```bash
# 1) acompanhar os e-mails
docker logs -f finduhouse-servidor-email

# 2) (em outro terminal) solicitar interesse
curl -X POST "http://localhost:8081/api/vagas/1/interesse?estudanteId=1"
```

**Esperado no log do servidor de e-mail:**

```
Total de emails recebidos: 2
Assunto: Interesse registrado na vaga: Quarto perto da PUC
Remetente: nao-responda@finduhouse.com
Destinatários: [maria.estudante@puc-campinas.edu.br]
Conteúdo:
Olá Maria Estudante,
...
----------------------
Assunto: Novo interesse na sua vaga: Quarto perto da PUC
Remetente: nao-responda@finduhouse.com
Destinatários: [carlos.proprietario@puc-campinas.edu.br]
Conteúdo:
Olá Carlos Proprietario,
...
----------------------
```

---

## 🗂️ Estrutura do projeto

```
pi4-2026-2-0101-grupo03-backend/
├── backend/                 # API Spring Boot (Java 21)
│   ├── src/main/java/       # código-fonte
│   ├── src/main/resources/  # application.properties
│   └── pom.xml
├── servidor-email/          # Servidor SMTP de teste (GreenMail)
│   ├── src/main/java/       # ServidorEmailApplication
│   └── pom.xml
├── docs/contratos/          # contratos CTxx
├── init.sql                 # schema + dados iniciais do banco
├── docker-compose.yml       # orquestração dos 3 containers
├── .env                     # variáveis (DB_NAME, DB_USER, DB_PASSWORD)
└── README.md
```

---

## 🔗 Endpoints principais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/vagas` | Cadastrar vaga |
| `POST` | `/api/vagas/{vagaId}/interesse?estudanteId={id}` | Solicitar interesse (envia dois e-mails) |
| `POST` | `/api/usuarios` | Cadastrar usuário |
| `POST` | `/api/auth/login` | Autenticar (retorna JWT) |

> Consulte os contratos em `backend/docs/contratos/` para detalhes.

---

## ❓ Problemas comuns

| Sintoma | Causa provável | Solução |
|---------|----------------|---------|
| `port is already allocated` | Porta 8081/2525/5432 ocupada | Pare o processo na porta ou ajuste no `docker-compose.yml` |
| E-mails não chegam | `application.properties` com host/porta errados | Confirme `finduhouse-servidor-email:25` e rebuild |
| Código não atualiza | Cache do Docker | `docker builder prune -f` + `docker-compose up -d --build` |
| Banco com schema antigo | Volume antigo | `docker-compose down -v` + `docker-compose up -d --build` |
| `role "postgres" does not exist` | Credenciais erradas | Use `-U finduhouse -d finduhouse` (ver `.env`) |

---

## 📄 Licença

Projeto acadêmico — PUC-Campinas, PI4 2026-2. Uso restrito ao grupo e à disciplina.