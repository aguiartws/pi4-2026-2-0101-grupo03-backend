# 1. FindUHouse

Plataforma web que conecta estudantes universitários a moradias compatíveis (repúblicas, kitnets e apartamentos compartilhados) com base em perfil, hábitos e orçamento, substituindo a busca manual em grupos de WhatsApp e redes sociais.

## 2. Integrantes

| Nome                                    | RA       | Responde por       |
|-----------------------------------------|----------|--------------------|
| Ana Carolina Ferreira Alves Barcaro     | 25005077 | Frontend           |
| Ana Clara Coelho Chaves                 | 25003865 | Frontend           |
| Brenda Maia Bergamasco                  | 25010054 | Banco e Frontend   |
| Caio Cantarin Marques                   | 25006919 | Banco e Frontend   |
| Luis Filippi Aguiar da Rocha            | 25020954 | Backend            |
| Maria Júlia Hofstetter Trevisan Pereira | 25021039 | Backend e Socket   |


## 3. Repositórios
- Front-end: https://github.com/aguiartws/pi4-2026-2-0101-grupo03-frontend.git. O repositório contém o desenvolvimento do frontend da plataforma FindUHouse, responsável pela interface de interação com alunos e proprietários. Desenvolvido com HTML, CSS e JavaScript, apresenta as telas de cadastro, login, gerenciamento de perfis e busca de moradias. Também contempla recursos de visualização e filtragem de vagas, apresentação de informações das moradias e interação com as funcionalidades da plataforma. Conta ainda com configuração do servidor Nginx e Docker Compose para execução da aplicação.

- Back-end:https://github.com/aguiartws/pi4-2026-2-0101-grupo03-backend.git. O repositório contém o desenvolvimento do backend da plataforma FindUHouse, responsável pelo processamento das funcionalidades e pela comunicação com o banco de dados. Desenvolvido em Java com Spring Boot, disponibiliza APIs para cadastro e autenticação de usuários, gerenciamento de vagas de moradia e registro de interesse dos estudantes. Também conta com um banco de dados PostgreSQL, um servidor de e-mail e configuração de containers Docker, que permitem executar os serviços de forma integrada.


---

## 4. Pré-requisitos

Para executar o projeto em uma máquina limpa, é necessário instalar:

- **Docker Desktop 4.x ou superior**, com Docker Compose V2.
- **Git 2.x ou superior**, para clonar os repositórios.

Não é necessário instalar Java, Maven, PostgreSQL ou Nginx na máquina, pois esses serviços são executados em containers Docker.

Para verificar as instalações, execute:

```bash
docker --version
docker compose version
git --version
```

## 5. Como subir o projeto

### Passo 1 – Clonar os repositórios

Abra o terminal e execute:

```bash
git clone https://github.com/aguiartws/pi4-2026-2-0101-grupo03-backend.git

git clone https://github.com/aguiartws/pi4-2026-2-0101-grupo03-frontend.git
```

### Passo 2 – Configurar e subir o backend

Acesse a pasta do backend:

```bash
cd pi4-2026-2-0101-grupo03-backend
```

Crie o arquivo `.env` a partir do modelo disponibilizado:

```bash
cp .env.example .env
```

Abra o arquivo `.env` e configure as variáveis necessárias para execução do projeto, incluindo as credenciais do servidor de e-mail.

O projeto utiliza PostgreSQL em container Docker como banco de dados local, podendo também ser configurado para utilizar um banco PostgreSQL na nuvem (Neon).

Por questões de segurança, o arquivo `.env` não é versionado no Git. O arquivo `.env.example` disponibiliza apenas o modelo das configurações necessárias.

Após preencher as variáveis, execute:

```bash
docker compose up -d --build
```

Esse comando inicia a API, o banco de dados PostgreSQL e o servidor de e-mail.

### Passo 3 – Preparar o frontend

O frontend utiliza uma rede Docker externa chamada `finduhouse-net`. Caso ela ainda não exista, crie-a:

```bash
docker network create finduhouse-net
```

Acesse a pasta do frontend:

```bash
cd ../pi4-2026-2-0101-grupo03-frontend
```

Suba a aplicação:

```bash
docker compose up -d
```

### Passo 4 – Acessar a aplicação

Abra o navegador e acesse:

- **Frontend:** http://localhost:8080
- **Backend (API):** http://localhost:8081

Para verificar se os containers estão funcionando, execute:

```bash
docker ps
```

## 6. Como usar

### E1 – Cadastro de perfil do aluno
1. Acesse a tela de cadastro de estudante.
2. Preencha os dados pessoais solicitados.
3. Informe o e-mail institucional e os demais dados necessários.
4. Finalize o cadastro para criar seu perfil.

### E2 – Cadastro de vaga pela moradia
1. Acesse a plataforma com uma conta de proprietário.
2. Entre na área de cadastro de vagas.
3. Preencha as informações da moradia, como título, descrição, endereço, valor do aluguel e características do imóvel.
4. Salve o cadastro para disponibilizar a vaga na plataforma.

### E3 – Painel "Minhas vagas" do proprietário
1. Acesse sua conta de proprietário.
2. Entre na seção "Minhas vagas".
3. Visualize as vagas cadastradas.
4. Selecione uma vaga para consultar ou editar suas informações.
5. Salve as alterações realizadas.

### E4 – Login com e-mail institucional (aluno)
1. Acesse a tela de login do estudante.
2. Informe seu e-mail institucional e sua senha.
3. Clique em entrar para acessar sua conta.

### E5 – Busca com filtros de preço e distância
1. Acesse a área de busca de moradias.
2. Informe a faixa de preço desejada.
3. Defina a distância máxima em relação ao campus.
4. Aplique os filtros para visualizar as vagas correspondentes.

### E6 – Lista e mapa de vagas
1. Acesse a área de busca de moradias.
2. Visualize as vagas disponíveis em formato de lista.
3. Acesse a visualização em mapa para consultar a localização das moradias.
4. Selecione uma vaga para visualizar seus detalhes.

### E7 – Favoritos
1. Acesse a lista de vagas disponíveis.
2. Selecione o ícone de favorito na vaga desejada.
3. Acesse a seção "Favoritos" para consultar as moradias salvas.

### E8 – Questionário de convivência
1. Acesse seu perfil de estudante.
2. Entre na seção do questionário de convivência.
3. Responda às perguntas sobre seus hábitos e preferências de moradia.
4. Salve suas respostas para utilizá-las no cálculo de compatibilidade.

### E9 – Pontuação de compatibilidade estudante × vaga
1. Acesse a área de busca de moradias.
2. Certifique-se de que o questionário de convivência foi preenchido.
3. Selecione uma vaga para consultar sua pontuação de compatibilidade.
4. Visualize a nota apresentada e os critérios considerados na comparação.

### E10 – Cadastro de perfil do proprietário
1. Acesse a tela de cadastro de proprietário.
2. Preencha os dados pessoais solicitados.
3. Informe seu e-mail pessoal e os demais dados necessários.
4. Finalize o cadastro para criar seu perfil.

### E11 – Login com e-mail pessoal (proprietário)
1. Acesse a tela de login do proprietário.
2. Informe seu e-mail pessoal e sua senha.
3. Clique em entrar para acessar sua conta e gerenciar suas moradias.


## 7. Documentação
- - [Documentação de Projeto](docs/documento-de-projeto.md)