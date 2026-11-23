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

**Autenticação/autorização:** a requisição exige usuário autenticado com perfil de proprietário. O backend usa o ID do usuário autenticado e valida sua correspondência com `responsavelId`.

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| responsavelId | inteiro | sim | 7 |
| titulo | texto, máximo 255 caracteres | sim | Quarto individual próximo ao campus |
| descricao | texto | sim | Quarto mobiliado, banheiro compartilhado |
| endereco | texto | sim | Rua das Acácias, 123 |
| valorAluguel | decimal | sim | 1200.00 |
| numeroMoradores | inteiro | sim | 3 |
| regrasCasa | texto | sim | Não pode festas, silêncio após 22h |
| latitude | decimal | sim | -22.905 |
| longitude | decimal | sim | -47.060 |
| regras | objeto Regras | sim | (ver objeto abaixo) |

### Objeto Regras

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| aceitaBarulhoDepoisDas22 | booleano | sim | true |
| fuma | booleano | sim | false |
| aceitaAnimais | booleano | sim | true |
| gostaReceberVisitas | booleano | sim | true |
| gostaCozinhar | booleano | sim | true |
| estudaEmCasa | booleano | sim | true |
| aceitaDividirTarefas | booleano | sim | true |
| aceitaDividirQuarto | booleano | sim | false |
| aceitaFestas | booleano | sim | true |
| comunicativo | booleano | sim | true |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| vagaId | inteiro | sim | 101 |
| status | um de: ativa, inativa | sim | ativa |
| criadoEm | data e hora | sim | 2026-10-13T19:20:00 |

## Erros

| Quando | O que volta |
| :--- | :--- |
| Usuário autenticado não corresponde ao responsável informado ou não tem perfil de proprietário | Erro `ACESSO_NEGADO` |
| Dados inválidos (campos faltando) | Erro DADOS_INVALIDOS |
| Valor negativo | Erro VALOR_INVALIDO |