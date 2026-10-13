# CT1: cadastrarPerfil

## Cabeçalho

| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT1 - cadastrarPerfil |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E1 |
| **Como chamar** | POST /api/estudantes |

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| nome | texto | sim | Ana Souza |
| email | texto | sim | ana@puc-campinas.edu.br |
| telefone | texto | sim | +5519999990000 |
| senha | texto | sim | MinhaSenha123 |
| curso | texto | sim | Engenharia de Software |
| orcamentoMin | decimal | sim | 800.00 |
| orcamentoMax | decimal | sim | 1500.00 |
| distanciaMaxima | decimal | sim | 5.5 |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| estudanteId | inteiro | sim | 42 |
| nome | texto | sim | Ana Souza |
| email | texto | sim | ana@puc-campinas.edu.br |
| telefone | texto | sim | +5519999990000 |
| curso | texto | sim | Engenharia de Software |
| orcamentoMin | decimal | sim | 800.00 |
| orcamentoMax | decimal | sim | 1500.00 |
| distanciaMaxima | decimal | sim | 5.5 |
| criadoEm | data e hora | sim | 2026-10-13T19:20:00 |

## Erros

| Quando | O que volta |
| :--- | :--- |
| Campos obrigatórios não preenchidos | Erro DADOS_OBRIGATORIOS |
| E-mail já cadastrado | Erro EMAIL_JA_EXISTE |
| E-mail fora do domínio institucional | Erro EMAIL_INSTITUCIONAL_INVALIDO |
| Valores negativos para orçamento ou distância | Erro VALOR_INVALIDO |