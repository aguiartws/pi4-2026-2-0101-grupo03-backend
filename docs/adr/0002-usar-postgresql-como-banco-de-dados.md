# ADR 0002: Usar PostgreSQL como banco de dados

**Status:** aceito

**Contexto:** O sistema precisa armazenar dados de forma persistente e o backend em Java com Spring Boot precisa acessá-los. O grupo precisa de um banco confiável, gratuito e com bom suporte no ecossistema Java. O projeto é acadêmico, então o custo de licença e a facilidade de instalar o banco em todas as máquinas do grupo pesam na escolha.

**Decisão:** Adotar o PostgreSQL como banco de dados relacional do projeto.

**Alternativas consideradas:**
- MySQL/MariaDB: descartado por não trazer vantagem clara sobre o PostgreSQL para este projeto, que se beneficia de um banco com SQL mais completo e recursos avançados.
- Banco em memória (H2) ou SQLite: descartado porque são pensados para testes ou uso local, e não representam bem um banco de servidor.
- Banco NoSQL (por exemplo, MongoDB): descartado porque os dados do projeto tendem a ser relacionais e o grupo já trabalha com modelagem relacional.

**Consequências:**
- Positivas: banco gratuito e de código aberto; suporte maduro no Spring Boot por meio de JDBC e JPA; garante integridade dos dados com transações e chaves estrangeiras.
- Negativas: todos os integrantes precisam instalar e configurar o PostgreSQL localmente, ou usar um contêiner; o servidor de banco passa a ser mais uma peça a manter e a hospedar; mudanças no esquema exigem controle de versão (migrações) para que o grupo não fique com bancos diferentes em cada máquina.
