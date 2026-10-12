# CT9: buscarVagas

## Cabeçalho

| Linha | Valor |
|---|---|
| **ID e nome** | CT9 - buscarVagas |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E5 - Busca com filtros de preço e distância |
| **Como chamar** | `GET /api/vagas?precoMaximo=1500&distanciaMaxima=5` |

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `precoMaximo` | decimal | Sim | `1500.00` |
| `distanciaMaxima` | decimal | Sim | `5.0` |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `vagas` | lista de (objeto vaga) | Sim | Lista de vagas que atendem aos filtros |

Cada objeto `vaga` pode conter os seguintes campos:

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `id` | inteiro | Sim | `1` |
| `titulo` | texto | Sim | `Kitnet próxima à universidade` |
| `preco` | decimal | Sim | `1200.00` |
| `distanciaCampus` | decimal | Sim | `3.5` |
| `descricao` | texto | Não | `Kitnet mobiliada para estudante` |

## Erros

| Condição | O que retorna |
|---|---|
| Preço máximo ou distância máxima não informado | Erro `DADOS_OBRIGATORIOS` |
| Preço máximo ou distância máxima com valor inválido ou negativo | Erro `FILTROS_INVALIDOS` |
| Nenhuma vaga atende aos dois filtros | Lista vazia de vagas |
| Falha interna durante a busca | Erro `ERRO_INTERNO` |