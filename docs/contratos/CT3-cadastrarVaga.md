# CT3: cadastrarVaga

## Cabeçalho

| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT3 - cadastrarVaga |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E2 |
| **Como chamar** | POST /api/vagas |

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| responsavelId | inteiro | sim | 7 |
| titulo | texto | sim | Quarto individual próximo ao campus |
| descricao | texto | sim | Quarto mobiliado, banheiro compartilhado |
| endereco | texto | sim | Rua das Acácias, 123 |
| valorAluguel | decimal | sim | 1200.00 |
| numeroMoradores | inteiro | sim | 3 |
| regrasCasa | texto | sim | Não pode festas, silêncio após 22h |
| latitude | decimal | sim | -22.905 |
| longitude | decimal | sim | -47.060 |
| regras | objeto RegrasVaga | sim | (ver objeto abaixo) |

### Objeto RegrasVaga

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| horarioDormirSemana | um de: antes_das_22, entre_22_e_0, depois_da_0 | sim | entre_22_e_0 |
| horarioDormirFimSemana | um de: antes_das_22, entre_22_e_0, depois_da_0 | sim | depois_da_0 |
| horarioAcordarSemana | um de: antes_das_6, entre_6_e_8, depois_das_8 | sim | entre_6_e_8 |
| horarioAcordarFimSemana | um de: antes_das_6, entre_6_e_8, depois_das_8 | sim | depois_das_8 |
| nivelLimpeza | um de: baixo, medio, alto | sim | alto |
| nivelOrganizacao | um de: baixo, medio, alto | sim | medio |
| toleranciaRuido | um de: baixa, media, alta | sim | media |
| frequenciaVisitas | um de: nunca, as_vezes, frequentemente | sim | as_vezes |
| frequenciaFestas | um de: nunca, as_vezes, frequentemente | sim | as_vezes |
| temPet | booleano | sim | false |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| vagaId | inteiro | sim | 101 |
| status | um de: ativa, em_analise, inativa | sim | ativa |
| criadoEm | data e hora | sim | 2026-10-13T19:20:00 |

## Erros

| Quando | O que volta |
| :--- | :--- |
| Responsável não encontrado | Erro RESPONSAVEL_NAO_ENCONTRADO |
| Dados inválidos (campos faltando) | Erro DADOS_INVALIDOS |
| Valor negativo | Erro VALOR_INVALIDO |