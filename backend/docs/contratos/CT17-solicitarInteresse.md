# CT17: solicitarInteresse

## Cabeçalho

| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT17 - solicitarInteresse |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E10 |
| **Como chamar** | POST /api/vagas/{vagaId}/interesse |

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| vagaId | inteiro | sim | 17 |
| estudanteId | inteiro | sim | 42 |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| sucesso | booleano | sim | true |
| emailEnviado | booleano | sim | true |
| mensagem | texto | sim | Interesse registrado. Verifique seu e-mail. |

## Erros

| Condição | O que retorna |
| :--- | :--- |
| Vaga não encontrada | Erro VAGA_NAO_ENCONTRADA |
| Estudante não encontrado | Erro ESTUDANTE_NAO_ENCONTRADO |
| Interesse já registrado por este estudante | Erro INTERESSE_JA_REGISTRADO |
| Falha ao enviar e-mail | Erro FALHA_ENVIO_EMAIL |
| Falha interna durante a requisição | Erro ERRO_INTERNO |