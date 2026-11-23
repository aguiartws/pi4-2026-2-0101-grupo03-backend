# Documento de Projeto — FindUHouse

## 1. Identificação

**Projeto:** FindUHouse  
**Componente documentado:** Backend  
**Tecnologia principal:** Java 21 com Spring Boot 3.5.6  
**Banco de dados:** PostgreSQL 16  
**Orquestração local:** Docker Compose

## 2. Objetivo do sistema

O FindUHouse é uma plataforma web voltada à conexão entre estudantes universitários e opções de moradia. O sistema oferece recursos de cadastro e autenticação de estudantes e proprietários, publicação e consulta de vagas, filtros de busca, favoritos, questionário de convivência, compatibilidade e manifestação de interesse.

Esta documentação descreve o backend e os arquivos de infraestrutura presentes neste repositório. A interface frontend está em repositório separado.

## 3. Organização da documentação

A documentação técnica está dividida nos seguintes arquivos:

| Documento | Conteúdo |
|---|---|
| [Escopo](ESCOPO-0101-grupo03.md) | Escopo e delimitação do projeto |
| [Arquitetura](arquitetura.md) | Visão da arquitetura, contêineres, serviços, portas e comunicação |
| [Banco de Dados](banco-de-dados.md) | Modelo relacional, tabelas, campos, chaves e cardinalidades |
| [Contratos](contratos/) | Especificações das fronteiras HTTP, banco e serviços |
| [ADRs](adr/) | Registros das decisões arquiteturais |

Para consultar o índice completo dos documentos, acesse [README da documentação](README.md).

## 4. Arquitetura

O backend é uma aplicação Java 21 com Spring Boot 3.5.6. A aplicação expõe uma API HTTP, utiliza Spring Data JPA para persistência em PostgreSQL e integra-se a serviços auxiliares de e-mail, validação de CEP e armazenamento de imagens.

A execução local é descrita em [Arquitetura](arquitetura.md), que contém o diagrama de contêineres, a tabela de serviços Docker, as portas e as dependências entre os serviços.

## 5. Banco de dados

O sistema utiliza PostgreSQL. O modelo inicial está definido em `init.sql`, na raiz do repositório. As tabelas documentadas são:

- `usuario`;
- `estudante`;
- `proprietario`;
- `vaga`;
- `favorito`;
- `interesse`;
- `questionario_estudante`;
- `vaga_foto`.

O diagrama entidade-relacionamento, a descrição dos campos e as cardinalidades estão em [Banco de Dados](banco-de-dados.md).

## 6. Contratos e decisões arquiteturais

Os contratos estão organizados em arquivos individuais identificados de CT1 a CT27. Eles registram as operações, fronteiras, participantes, entradas, saídas e erros esperados.

As decisões arquiteturais estão registradas em ADRs:

- ADR 0001 — uso de Java com Spring Boot no backend;
- ADR 0002 — uso de PostgreSQL como banco de dados;
- ADR 0003 — uso de Docker para executar o backend e o banco de dados.

## 7. Execução do projeto

As instruções de execução estão no [README principal do repositório](../../README.md). A configuração atual do Docker Compose utiliza uma rede externa chamada `finduhouse-net`, que precisa existir antes da inicialização dos serviços. Consulte também a seção de rede da [documentação de arquitetura](arquitetura.md).

## 8. Pontos que exigem conferência antes da entrega

Os itens abaixo foram identificados ao comparar os arquivos de configuração, o script SQL e os documentos existentes. Devem ser conferidos pelo grupo antes de considerar a documentação finalizada:

1. **Rede externa:** o Compose depende da rede `finduhouse-net`, criada previamente. Isso precisa estar explícito no procedimento de execução.
2. **Variáveis de ambiente:** conferir se o `.env.example` contém todas as variáveis exigidas e apenas valores demonstrativos, sem credenciais reais.
3. **Persistência de denúncias:** existem contratos CT19 e CT20, mas o `init.sql` não declara tabela de denúncias. Verificar a implementação e documentar a persistência real.
4. **Questionário:** o banco registra dez características booleanas de convivência. Conferir se os contratos e a documentação funcional usam os mesmos campos.
5. **Estados da vaga:** o `init.sql` restringe o status a `ativa` e `inativa`. Contratos que mencionem outros estados precisam ser compatibilizados com a implementação.
6. **Configuração JPA:** `application.properties` define `ddl-auto=validate`, enquanto o Compose sobrescreve essa propriedade com `update`. Registrar e validar a configuração pretendida para o ambiente de execução.
