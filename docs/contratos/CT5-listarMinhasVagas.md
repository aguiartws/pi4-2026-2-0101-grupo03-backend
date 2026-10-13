# CT5: listarMinhasVagas

## Cabeçalho

| Linha | Valor |
| :--- | :--- |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E3 |
| **Como chamar** | GET /responsaveis/{idResponsavel}/vagas |

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| idResponsavel | inteiro | sim | 7 |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| vagas | lista de objeto Vaga | sim | (ver objeto abaixo) |

### Objeto Vaga

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| id | inteiro | sim | 101 |
| titulo | texto | sim | Quarto individual próximo ao campus |
| endereco | texto | sim | Rua das Acácias, 123 |
| valorAluguel | decimal | sim | 1200.00 |
| status | um de: ativa, em_analise, inativa | sim | ativa |
| criadoEm | data e hora | sim | 2026-10-13T19:20:00 |

## Erros

| Quando | O que volta |
| :--- | :--- |
| Responsável não encontrado | Erro RESPONSAVEL_NAO_ENCONTRADO |
| Nenhuma vaga encontrada | Retorna lista vazia (não é erro) |