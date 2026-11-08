# CT15: questionario_estudante

## Cabeçalho

| Linha | Valor |
|---|---|
| **ID e nome** | CT15 - questionario_estudante |
| **Fronteira** | Banco |
| **Quem chama** | Backend |
| **Quem responde** | Banco |
| **Atende** | E8 - Questionário de convivência, E9 - Pontuação de compatibilidade |
| **Como chamar** | Tabela `questionario_estudante`, chave `id` |

## Entrada

Os campos gravados são os mesmos campos lidos.

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `id` | inteiro | Sim | `1` |
| `estudante_id` | inteiro | Sim | `42` |
| `aceita_barulho_depois_das_22` | booleano | Sim | `true` |
| `fuma` | booleano | Sim | `false` |
| `aceita_animais` | booleano | Sim | `true` |
| `gosta_receber_visitas` | booleano | Sim | `true` |
| `gosta_cozinhar` | booleano | Sim | `true` |
| `estuda_em_casa` | booleano | Sim | `true` |
| `aceita_dividir_tarefas` | booleano | Sim | `true` |
| `aceita_dividir_quarto` | booleano | Sim | `false` |
| `aceita_festas` | booleano | Sim | `true` |
| `comunicativo` | booleano | Sim | `true` |
| `atualizado_em` | data e hora | Sim | `2026-10-13T19:20:00` |

## Saída

Os campos gravados são os mesmos campos lidos (ver tabela acima).

## Erros

| Condição | O que retorna |
|---|---|
| O `estudante_id` não existe na tabela `estudante` | O banco recusa a gravação |
| Valor fora da lista permitida em algum campo | O banco recusa a gravação |