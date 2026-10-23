# CT13: listarFavoritos

## Cabeçalho

| Linha | Valor |
|---|---|
| **ID e nome** | CT13 - listarFavoritos |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E7 - Favoritos |
| **Como chamar** | `GET /api/favoritos?estudanteId=42` |

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `estudanteId` | inteiro | Sim | `42` |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `favoritos` | lista de objeto Vaga | Sim | Lista de vagas salvas pelo estudante |

Cada objeto `vaga` pode conter:

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `id` | inteiro | Sim | `15` |
| `titulo` | texto | Sim | `Kitnet próxima à universidade` |
| `endereco` | texto | Sim | `Rua das Flores, 100, Campinas` |
| `valorAluguel` | decimal | Sim | `1200.00` |
| `descricao` | texto | Não | `Kitnet mobiliada para estudante` |

## Erros

| Condição | O que retorna |
|---|---|
| Identificador do estudante não informado | Erro `DADOS_OBRIGATORIOS` |
| Estudante não encontrado | Erro `ESTUDANTE_NAO_ENCONTRADO` |
| Estudante não possui vagas favoritas | Lista vazia de favoritos |
| Falha interna durante a consulta | Erro `ERRO_INTERNO` |