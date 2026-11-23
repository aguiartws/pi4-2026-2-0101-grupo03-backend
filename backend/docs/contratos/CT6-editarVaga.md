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

**Autenticação/autorização:** exige usuário autenticado. O backend valida que o usuário tem perfil de proprietário e que a vaga pertence ao responsável autenticado.

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| vagaId | inteiro | sim | 101 |
| responsavelId | inteiro | sim | 7 |
| titulo | texto, máximo 255 caracteres | sim | Quarto individual próximo ao campus |
| descricao | texto | sim | Descrição atualizada |
| descricao | texto | sim | Descrição atualizada |
| endereco | texto | sim | Rua das Acácias, 123 |
| valorAluguel | decimal maior que zero | sim | 1300.00 |
| numeroMoradores | inteiro positivo | sim | 3 |
| regrasCasa | texto | sim | Sem festas durante a semana |
| latitude | decimal | sim | -22.905 |
| longitude | decimal | sim | -47.060 |
| regrasConvivencia | objeto Regras | sim | Ver as dez propriedades do CT3 |
| status | um de: ativa, inativa | sim | ativa |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| vagaId | inteiro | sim | 101 |
| descricao | texto | sim | Descrição atualizada |
| endereco | texto | sim | Rua das Acácias, 123 |
| valorAluguel | decimal maior que zero | sim | 1300.00 |
| numeroMoradores | inteiro positivo | sim | 3 |
| regrasCasa | texto | sim | Sem festas durante a semana |
| latitude | decimal | sim | -22.905 |
| longitude | decimal | sim | -47.060 |
| regrasConvivencia | objeto Regras | sim | Ver as dez propriedades do CT3 |
| status | um de: ativa, inativa | sim | ativa |
| atualizadoEm | data e hora | sim | 2026-10-13T19:20:00 |

## Erros

| Quando | O que volta |
| :--- | :--- |
| Vaga não encontrada | Erro VAGA_NAO_ENCONTRADA |
| Vaga não pertence ao responsável | Erro ACESSO_NEGADO |
| Valor inválido (negativo) | Erro VALOR_INVALIDO |