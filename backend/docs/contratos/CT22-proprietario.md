# CT22: proprietario 

## Cabeçalho

| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT22 - proprietario |
| **Fronteira** | Banco |
| **Quem chama** | Backend |
| **Quem responde** | Banco |
| **Atende** | E12 |
| **Como chamar** | Tabela proprietario, chave id |

## Entrada e Saída (os campos gravados e lidos são os mesmos)

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| id | inteiro | sim | 42 |
| nome | texto | sim | Ana Souza |
| email | texto | sim | ana.souza@gmail.br |
| telefone | texto | sim | +5519999990000 |
| criado_em | data e hora | sim | 2026-10-13T19:20:00 |
| atualizado_em | data e hora | sim | 2026-10-13T19:20:00 |

## Erros

| Quando | O que volta |
| :--- | :--- |
| E-mail duplicado | O banco recusa a gravação |
| Campos nulos | O banco recusa a gravação |
| id não encontrado em consulta | Retorna vazio |