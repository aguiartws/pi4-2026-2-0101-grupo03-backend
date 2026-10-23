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
| `horario_dormir_semana` | um de: `antes_das_22`, `entre_22_e_0`, `depois_da_0` | Sim | `entre_22_e_0` |
| `horario_dormir_fim_semana` | um de: `antes_das_22`, `entre_22_e_0`, `depois_da_0` | Sim | `depois_da_0` |
| `horario_acordar_semana` | um de: `antes_das_6`, `entre_6_e_8`, `depois_das_8` | Sim | `entre_6_e_8` |
| `horario_acordar_fim_semana` | um de: `antes_das_6`, `entre_6_e_8`, `depois_das_8` | Sim | `depois_das_8` |
| `nivel_limpeza` | um de: `baixo`, `medio`, `alto` | Sim | `alto` |
| `nivel_organizacao` | um de: `baixo`, `medio`, `alto` | Sim | `medio` |
| `tolerancia_ruido` | um de: `baixa`, `media`, `alta` | Sim | `media` |
| `frequencia_visitas` | um de: `nunca`, `as_vezes`, `frequentemente` | Sim | `as_vezes` |
| `frequencia_festas` | um de: `nunca`, `as_vezes`, `frequentemente` | Sim | `as_vezes` |
| `tem_pet` | booleano | Sim | `false` |
| `atualizado_em` | data e hora | Sim | `2026-10-13T19:20:00` |

## Saída

Os campos gravados são os mesmos campos lidos (ver tabela acima).

## Erros

| Condição | O que retorna |
|---|---|
| O `estudante_id` não existe na tabela `estudante` | O banco recusa a gravação |
| Valor fora da lista permitida em algum campo | O banco recusa a gravação |