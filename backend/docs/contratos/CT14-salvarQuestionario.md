# CT14: salvarQuestionario

## Cabeçalho

| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT14 - salvarQuestionario |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E8 |
| **Como chamar** | POST /api/questionario/questionario |

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| estudanteId | inteiro | sim | 42 |
| respostas | objeto Questionario | sim | Ver tabela abaixo |

Cada objeto `Questionario` contém os seguintes campos:

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| aceitaBarulhoDepoisDas22 | booleano | sim | true |
| fuma | booleano | sim | false |
| aceitaAnimais | booleano | sim | true |
| gostaReceberVisitas | booleano | sim | true |
| gostaCozinhar | booleano | sim | true |
| estudaEmCasa | booleano | sim | true |
| aceitaDividirTarefas | booleano | sim | true |
| aceitaDividirQuarto | booleano | sim | false |
| aceitaFestas | booleano | sim | true |
| comunicativo | booleano | sim | true |

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
| Falha interna durante a requisição | Erro ERRO_INTERNO |
