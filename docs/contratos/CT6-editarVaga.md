# CT6: editarVaga

## Cabeçalho

| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT6 - editarVaga |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E3 |
| **Como chamar** | PATCH /api/vagas/{vagaId} |

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| vagaId | inteiro | sim | 101 |
| valorAluguel | decimal | sim | 1300.00 |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| vagaId | inteiro | sim | 101 |
| valorAluguel | decimal | sim | 1300.00 |
| atualizadoEm | data e hora | sim | 2026-10-13T19:20:00 |

## Erros

| Quando | O que volta |
| :--- | :--- |
| Vaga não encontrada | Erro VAGA_NAO_ENCONTRADA |
| Vaga não pertence ao responsável | Erro ACESSO_NEGADO |
| Valor inválido (negativo) | Erro VALOR_INVALIDO |