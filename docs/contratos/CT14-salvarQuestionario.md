# CT14: salvarQuestionario

## Cabeçalho

| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT14 - salvarQuestionario |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E8 |
| **Como chamar** | POST /api/estudantes/{estudanteId}/questionario |

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| estudanteId | inteiro | sim | 42 |
| respostas | objeto Questionario | sim | Ver tabela abaixo |

Cada objeto `Questionario` contém os seguintes campos:

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

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| sucesso | booleano | sim | true |
| atualizadoEm | data e hora | sim | 2026-10-13T19:20:00 |

## Erros

| Condição | O que retorna |
| :--- | :--- |
| Estudante não existe | Erro ESTUDANTE_NAO_ENCONTRADO |
| Campos obrigatórios do questionário ausentes | Erro QUESTIONARIO_INCOMPLETO |
| Valor fora da lista em algum campo | Erro RESPOSTA_INVALIDA |
| Falha interna durante a requisição | Erro ERRO_INTERNO |