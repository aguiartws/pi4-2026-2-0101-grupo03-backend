# CT24: obterDetalhesVaga

## Cabeçalho
| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT24 - obterDetalhesVaga |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E9 (e página pública da vaga) |
| **Como chamar** | GET /api/vagas/{vagaId} |

## Entrada
| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| vagaId | inteiro | sim | 17 |

## Saída
| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| id | inteiro | sim | 17 |
| titulo | texto | sim | Quarto individual próximo ao campus |
| descricao | texto | sim | Quarto mobiliado, banheiro compartilhado |
| endereco | texto | sim | Rua das Acácias, 123 |
| valorAluguel | decimal | sim | 1200.00 |
| numeroMoradores | inteiro | sim | 3 |
| regrasCasa | texto | sim | Não pode festas, silêncio após 22h |
| latitude | decimal | sim | -22.905 |
| longitude | decimal | sim | -47.060 |
| status | um de: ativa, em_analise, inativa | sim | ativa |
| regrasConvivência | objeto RegrasVaga | sim | (As 10 regras do CT4) |

## Erros
| Quando | O que volta |
| :--- | :--- |
| Vaga não encontrada | Erro VAGA_NAO_ENCONTRADA |
| Falha interna durante a requisição | Erro ERRO_INTERNO |