# CT19: denunciarVaga

## Cabeçalho

| Linha | Valor |
|---|---|
| **ID e nome** | CT19 - denunciarVaga |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E11 - Denúncia de anúncio |
| **Como chamar** | `POST /api/vagas/{vagaId}/denuncia` |

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `vagaId` | inteiro | Sim | `17` |
| `estudanteId` | inteiro | Sim | `42` |
| `motivo` | um de: `informacao_falsa`, `golpe_financeiro`, `conteudo_inadequado`, `imovel_inexistente`, `outro` | Sim | `golpe_financeiro` |
| `descricao` | texto | Sim | `O anúncio pede depósito de R$ 500 antes da visita` |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `sucesso` | booleano | Sim | `true` |
| `denunciaId` | inteiro | Sim | `5` |
| `mensagem` | texto | Sim | `Denúncia registrada. A vaga está em análise.` |

## Erros

| Condição | O que retorna |
|---|---|
| Vaga não encontrada | Erro `VAGA_NAO_ENCONTRADA` |
| Estudante não encontrado | Erro `ESTUDANTE_NAO_ENCONTRADO` |
| Motivo fora da lista permitida | Erro `MOTIVO_INVALIDO` |
| Descrição vazia | Erro `DESCRICAO_OBRIGATORIA` |
| Denúncia já registrada por este estudante para esta vaga | Erro `DENUNCIA_DUPLICADA` |
| Falha interna durante a requisição | Erro `ERRO_INTERNO` |