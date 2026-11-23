# Documentação técnica do FindUHouse

Esta pasta reúne a documentação de projeto do backend FindUHouse.

## Documentos principais

- [Documento de Projeto](documento-de-projeto.md) — índice da documentação e visão consolidada.
- [Escopo](ESCOPO-0101-grupo03.md) — escopo registrado para o projeto.
- [Arquitetura](arquitetura.md) — serviços, contêineres, portas e comunicação.
- [Banco de Dados](banco-de-dados.md) — diagrama entidade-relacionamento, tabelas, campos e cardinalidades.

## Decisões de arquitetura (ADRs)

- [ADR 0001 — Java com Spring Boot no backend](adr/0001-usar-java-com-spring-boot-no-backend.md)
- [ADR 0002 — PostgreSQL como banco de dados](adr/0002-usar-postgresql-como-banco-de-dados.md)
- [ADR 0003 — Docker para executar o backend e o banco](adr/0003-usar-docker-para-executar-o-backend-e-o-banco.md)

## Contratos

| ID | Documento |
|---|---|
| CT1 | [cadastrarPerfil](contratos/CT1-cadastrarPerfil.md) |
| CT2 | [estudante](contratos/CT2-estudante.md) |
| CT3 | [cadastrarVaga](contratos/CT3-cadastrarVaga.md) |
| CT4 | [vaga](contratos/CT4-vaga.md) |
| CT5 | [listarMinhasVagas](contratos/CT5-listarMinhasVagas.md) |
| CT6 | [editarVaga](contratos/CT6-editarVaga.md) |
| CT7 | [autenticarUsuario](contratos/CT7-autenticarUsuario.md) |
| CT8 | [usuario](contratos/CT8-usuario.md) |
| CT9 | [buscarVagas](contratos/CT9-buscarVagas.md) |
| CT10 | [listarVagasMapa](contratos/CT10-listarVagasMapa.md) |
| CT11 | [favoritarVaga](contratos/CT11-favoritarVaga.md) |
| CT12 | [favorito](contratos/CT12-favorito.md) |
| CT13 | [listarFavoritos](contratos/CT13-listarFavoritos.md) |
| CT14 | [salvarQuestionario](contratos/CT14-salvarQuestionario.md) |
| CT15 | [questionario_estudante](contratos/CT15-questionario_estudante.md) |
| CT16 | [obterDetalhesVagaCompatibilidade](contratos/CT16-obterDetalhesVagaCompatibilidade.md) |
| CT17 | [solicitarInteresse](contratos/CT17-solicitarInteresse.md) |
| CT18 | [enviarEmailNotificacao](contratos/CT18-enviarEmailNotificacao.md) |
| CT19 | [denunciarVaga](contratos/CT19-denunciarVaga.md) |
| CT20 | [denuncia](contratos/CT20-denuncia.md) |
| CT21 | [cadastrarProprietario](contratos/CT21-cadastrarProprietario.md) |
| CT22 | [proprietario](contratos/CT22-proprietario.md) |
| CT23 | [autenticarProprietario](contratos/CT23-autenticarProprietario.md) |
| CT24 | [obterDetalhesVaga](contratos/CT24-obterDetalhesVaga.md) |
| CT25 | [obterQuestionarioEstudante](contratos/CT25-obterQuestionarioEstudante.md) |
| CT26 | [interesse](contratos/CT26-interesse.md) |
| CT27 | [perfilUsuario](contratos/CT27-perfilUsuario.md) |

## Observação sobre atualização

Os contratos descrevem as fronteiras e os dados previstos para as operações. Antes de alterar um contrato, compare-o com os controllers, DTOs, serviços e entidades atuais para manter a documentação sincronizada com o comportamento implementado.
