# CT2: estudante

## Cabeçalho

| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT2 - estudante |
| **Fronteira** | Banco |
| **Quem chama** | Backend |
| **Quem responde** | Banco |
| **Atende** | E1, E10 |
| **Como chamar** | Tabela estudante, chave id |

## Entrada e Saída (os campos gravados e lidos são os mesmos)

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| id | inteiro | sim | 42 |
| nome | texto | sim | Ana Souza |
| email | texto | sim | ana@puccampinas.edu.br |
| telefone | texto | sim | +5519999990000 |
| curso | texto | sim | Engenharia de Software |
| criado_em | data e hora | sim | 2026-10-13T19:20:00 |
| atualizado_em | data e hora | sim | 2026-10-13T19:20:00 |
| foto_url | texto ou nulo | não | https://.../foto.jpg |
| sobre_mim | texto ou nulo | não | Estudante de Engenharia de Software |

**Observação:** `cidade` pertence à tabela `usuario`, não à tabela `estudante`. O cadastro de estudante também cria/associa o registro correspondente em `usuario`.

## Erros

| Quando | O que volta |
| :--- | :--- |
| E-mail duplicado | O banco recusa a gravação |
| Campos nulos | O banco recusa a gravação |
| id não encontrado em consulta | Retorna vazio |