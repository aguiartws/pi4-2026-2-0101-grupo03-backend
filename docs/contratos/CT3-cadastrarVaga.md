# CT3: cadastrarVaga

## Cabeçalho

| Linha | Valor |
| :--- | :--- |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E2 |
| **Como chamar** | POST /vagas |

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| idResponsavel | inteiro | sim | 7 |
| titulo | texto | sim | Quarto individual próximo ao campus |
| descricao | texto | sim | Quarto mobiliado, banheiro compartilhado |
| endereco | texto | sim | Rua das Acácias, 123 |
| valorAluguel | decimal | sim | 1200.00 |
| numeroMoradores | inteiro | sim | 3 |
| regrasCasa | texto | sim | Não pode festas, silêncio após 22h |
| latitude | decimal | sim | -22.905 |
| longitude | decimal | sim | -47.060 |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| idVaga | inteiro | sim | 101 |
| status | um de: ativa, em_analise, inativa | sim | ativa |
| criadoEm | data e hora | sim | 2026-10-13T19:20:00 |

## Erros

| Quando | O que volta |
| :--- | :--- |
| Responsável não encontrado | Erro RESPONSAVEL_NAO_ENCONTRADO |
| Dados inválidos (campos faltando) | Erro DADOS_INVALIDOS |
| Valor negativo | Erro VALOR_INVALIDO |