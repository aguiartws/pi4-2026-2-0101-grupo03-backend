# CT14: salvarQuestionario

## Cabeçalho

| Linha | Valor |
|---|---|
| **ID e nome** | CT14 - salvarQuestionario |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E8 - Questionário de convivência |
| **Como chamar** | `POST /api/estudantes/{estudanteId}/questionario` |

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `estudanteId` | inteiro | Sim | `42` |
| `respostas` | objeto Questionario | Sim | Ver tabela abaixo |

Cada objeto `Questionario` contém os seguintes campos:

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `horarioDormirSemana` | um de: `antes_das_22`, `entre_22_e_0`, `depois_da_0` | Sim | `entre_22_e_0` |
| `horarioDormirFimSemana` | um de: `antes_das_22`, `entre_22_e_0`, `depois_da_0` | Sim | `depois_da_0` |
| `horarioAcordarSemana` | um de: `antes_das_6`, `entre_6_e_8`, `depois_das_8` | Sim | `entre_6_e_8` |
| `horarioAcordarFimSemana` | um de: `antes_das_6`, `entre_6_e_8`, `depois_das_8` | Sim | `depois_das_8` |
| `nivelLimpeza` | um de: `baixo`, `medio`, `alto` | Sim | `alto` |
| `nivelOrganizacao` | um de: `baixo`, `medio`, `alto` | Sim | `medio` |
| `toleranciaRuido` | um de: `baixa`, `media`, `alta` | Sim | `media` |
| `frequenciaVisitas` | um de: `nunca`, `as_vezes`, `frequentemente` | Sim | `as_vezes` |
| `frequenciaFestas` | um de: `nunca`, `as_vezes`, `frequentemente` | Sim | `as_vezes` |
| `temPet` | booleano | Sim | `false` |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `sucesso` | booleano | Sim | `true` |
| `atualizadoEm` | data e hora | Sim | `2026-10-13T19:20:00` |

## Erros

| Condição | O que retorna |
|---|---|
| Estudante não existe | Erro `ESTUDANTE_NAO_ENCONTRADO` |
| Menos de 10 respostas enviadas | Erro `QUESTIONARIO_INCOMPLETO` |
| Valor fora da lista em algum campo | Erro `RESPOSTA_INVALIDA` |
| Falha interna durante a requisição | Erro `ERRO_INTERNO` |