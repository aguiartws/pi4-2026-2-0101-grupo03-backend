# CT21: cadastrarProprietario

## Cabeçalho

| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT21 - cadastrarProprietario |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E12 |
| **Como chamar** | POST /api/proprietarios |

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| nome | texto | sim | Ana Souza |
| email | texto | sim | ana.souza@gmail.br |
| telefone | texto | sim | +5519999990000 |
| senha | texto | sim | MinhaSenha123 |


## Saída

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| proprietarioId | inteiro | sim | 42 |
| nome | texto | sim | Ana Souza |
| email | texto | sim | ana.souza@gmail.br |
| telefone | texto | sim | +5519999990000 |
| criadoEm | data e hora | sim | 2026-10-13T19:20:00 |

## Erros

| Quando | O que volta |
| :--- | :--- |
| Campos obrigatórios não preenchidos | Erro DADOS_OBRIGATORIOS |
| E-mail já cadastrado | Erro EMAIL_JA_EXISTE |
