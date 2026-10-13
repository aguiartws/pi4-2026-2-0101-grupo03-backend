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
| email | texto | sim | ana@puc-campinas.edu.br |
| telefone | texto | sim | +5519999990000 |
| curso | texto | sim | Engenharia de Software |
| orcamento_min | decimal | sim | 800.00 |
| orcamento_max | decimal | sim | 1500.00 |
| distancia_maxima | decimal | sim | 5.5 |
| criado_em | data e hora | sim | 2026-10-13T19:20:00 |
| atualizado_em | data e hora | sim | 2026-10-13T19:20:00 |

## Erros

| Quando | O que volta |
| :--- | :--- |
| E-mail duplicado | O banco recusa a gravação |
| Campos nulos | O banco recusa a gravação |
| id não encontrado em consulta | Retorna vazio |