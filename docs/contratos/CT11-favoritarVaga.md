# CT11: favoritarVaga

## Cabeçalho

| Linha | Valor |
|---|---|
| **ID e nome** | CT11 - favoritarVaga |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E7 - Favoritos |
| **Como chamar** | `POST /api/favoritos` |

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `estudanteId` | inteiro | Sim | `42` |
| `vagaId` | inteiro | Sim | `15` |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `mensagem` | texto | Sim | `Vaga favoritada com sucesso` |
| `estudanteId` | inteiro | Sim | `42` |
| `vagaId` | inteiro | Sim | `15` |

## Erros

| Condição | O que retorna |
|---|---|
| Identificador do estudante ou da vaga não informado | Erro `DADOS_OBRIGATORIOS` |
| Estudante não encontrado | Erro `ESTUDANTE_NAO_ENCONTRADO` |
| Vaga não encontrada | Erro `VAGA_NAO_ENCONTRADA` |
| Vaga já está nos favoritos do estudante | Erro `VAGA_JA_FAVORITADA` |
| Falha interna durante a operação | Erro `ERRO_INTERNO` |