# CT9: buscarVagas

## Cabeçalho

| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT9 - buscarVagas |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E5 |
| **Como chamar** | `GET /api/vagas?precoMaximo=1500&distanciaMaxima=5` |

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| precoMaximo | decimal | sim | 1500.00 |
| distanciaMaxima | decimal | sim | 5.0 |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| vagas | lista de objeto Vaga | sim | (ver objeto abaixo) |

### Objeto Vaga

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| id | inteiro | sim | 101 |
| titulo | texto | sim | Kitnet próxima à universidade |
| valorAluguel | decimal | sim | 1200.00 |
| distanciaCampus | decimal | sim | 3.5 |
| descricao | texto | não | Kitnet mobiliada para estudante |

## Erros

| Quando | O que volta |
| :--- | :--- |
| Preço máximo ou distância máxima não informado | Erro `DADOS_OBRIGATORIOS` |
| Preço máximo ou distância máxima com valor inválido ou negativo | Erro `FILTROS_INVALIDOS` |
| Nenhuma vaga atende aos dois filtros | Lista vazia de vagas |
| Falha interna durante a busca | Erro `ERRO_INTERNO` |