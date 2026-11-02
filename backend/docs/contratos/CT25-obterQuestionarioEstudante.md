# CT25: obterQuestionarioEstudante

## Cabeçalho
| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT25 - obterQuestionarioEstudante |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E8, E9 |
| **Como chamar** | GET /api/estudantes/{estudanteId}/questionario |

## Entrada
| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| estudanteId | inteiro | sim | 42 |

## Saída
| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| horarioDormirSemana | um de: antes_das_22, entre_22_e_0, depois_da_0 | sim | entre_22_e_0 |
| horarioDormirFimSemana | um de: antes_das_22, entre_22_e_0, depois_da_0 | sim | depois_da_0 |
| horarioAcordarSemana | um de: antes_das_6, entre_6_e_8, depois_das_8 | sim | entre_6_e_8 |
| horarioAcordarFimSemana | um de: antes_das_6, entre_6_e_8, depois_das_8 | sim | depois_das_8 |
| nivelLimpeza | um de: baixo, medio, alto | sim | alto |
| nivelOrganizacao | um de: baixo, medio, alto | sim | medio |
| toleranciaRuido | um de: baixa, media, alta | sim | media |
| frequenciaVisitas | um de: nunca, as_vezes, frequentemente | sim | as_vezes |
| frequenciaFestas | um de: nunca, as_vezes, frequentemente | sim | as_vezes |
| temPet | booleano | sim | false |

## Erros
| Quando | O que volta |
| :--- | :--- |
| Estudante não encontrado | Erro ESTUDANTE_NAO_ENCONTRADO |
| Questionário ainda não preenchido | Erro QUESTIONARIO_NAO_PREENCHIDO |
| Falha interna durante a requisição | Erro ERRO_INTERNO |