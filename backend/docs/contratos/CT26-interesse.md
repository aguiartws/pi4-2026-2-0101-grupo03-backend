# CT26: interesse

## Cabeçalho
| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT26 - interesse |
| **Fronteira** | Banco |
| **Quem chama** | Backend |
| **Quem responde** | Banco |
| **Atende** | E10 |
| **Como chamar** | Tabela interesse, chave id |

## Entrada e Saída (os campos gravados e lidos são os mesmos)
| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| id | inteiro | sim | 1 |
| estudante_id | inteiro | sim | 42 |
| vaga_id | inteiro | sim | 17 |
| criado_em | data e hora | sim | 2026-10-13T19:20:00 |
| status_envio_estudante | um de: pendente, enviado, falhou | sim | enviado |
| status_envio_proprietario | um de: pendente, enviado, falhou | sim | enviado |

## Erros
| Quando | O que volta |
| :--- | :--- |
| O estudante_id não existe na tabela estudante | O banco recusa a gravação |
| O vaga_id não existe na tabela vaga | O banco recusa a gravação |
| O vínculo entre estudante e vaga já existe | O banco recusa a gravação (impede duplicidade) |