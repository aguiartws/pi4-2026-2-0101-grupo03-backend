# CT4: vaga

## Cabeçalho

| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT4 - vaga |
| **Fronteira** | Banco |
| **Quem chama** | Backend |
| **Quem responde** | Banco |
| **Atende** | E2, E3, E5, E6, E9, E10, E11 |
| **Como chamar** | Tabela vaga, chave id |

## Entrada e Saída (os campos gravados e lidos são os mesmos)

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| id | inteiro | sim | 101 |
| responsavel_id | inteiro | sim | 7 |
| titulo | texto | sim | Quarto individual próximo ao campus |
| descricao | texto | sim | Quarto mobiliado, banheiro compartilhado |
| endereco | texto | sim | Rua das Acácias, 123 |
| valor_aluguel | decimal | sim | 1200.00 |
| numero_moradores | inteiro | sim | 3 |
| regras_casa | texto | sim | Não pode festas, silêncio após 22h |
| latitude | decimal | sim | -22.905 |
| longitude | decimal | sim | -47.060 |
| status | um de: ativa, em_analise, inativa | sim | ativa |
| criado_em | data e hora | sim | 2026-10-13T19:20:00 |
| atualizado_em | data e hora | sim | 2026-10-13T19:20:00 |

## Erros

| Quando | O que volta |
| :--- | :--- |
| responsavel_id não existe na tabela usuario | O banco recusa a gravação |
| Campos nulos | O banco recusa a gravação |