# CT10: listarVagasMapa

## Cabeçalho

| Linha | Valor |
|---|---|
| **ID e nome** | CT10 - listarVagasMapa |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E6 - Lista e mapa de vagas |
| **Como chamar** | `GET /api/vagas/mapa` |

## Entrada

Não tem.

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `vagas` | lista de objeto Vaga | Sim | Lista de vagas disponíveis |

Cada objeto `vaga` contém os seguintes campos:

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `id` | inteiro | Sim | `1` |
| `titulo` | texto | Sim | `Kitnet próxima à universidade` |
| `endereco` | texto | Sim | `Rua das Flores, 100, Campinas` |
| `valorAluguel` | decimal | Sim | `1200.00` |
| `latitude` | decimal | Sim | `-22.83365` |
| `longitude` | decimal | Sim | `-47.05197` |

## Erros

| Condição | O que retorna |
|---|---|
| Não existem vagas ativas cadastradas | Lista vazia de vagas |
| Falha ao consultar as vagas no banco de dados | Erro `ERRO_INTERNO` |
| Falha interna durante a requisição | Erro `ERRO_INTERNO` |