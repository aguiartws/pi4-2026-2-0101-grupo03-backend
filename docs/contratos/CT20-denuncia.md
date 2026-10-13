# CT20: denuncia

## Cabeçalho

| Linha | Valor |
|---|---|
| **ID e nome** | CT20 - denuncia |
| **Fronteira** | Banco |
| **Quem chama** | Backend |
| **Quem responde** | Banco |
| **Atende** | E11 - Denúncia de anúncio |
| **Como chamar** | Tabela `denuncia`, chave `id` |

## Entrada

Os campos gravados são os mesmos campos lidos.

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `id` | inteiro | Sim | `5` |
| `vaga_id` | inteiro | Sim | `17` |
| `estudante_id` | inteiro | Sim | `42` |
| `motivo` | um de: `informacao_falsa`, `golpe_financeiro`, `conteudo_inadequado`, `imovel_inexistente`, `outro` | Sim | `golpe_financeiro` |
| `descricao` | texto | Sim | `O anúncio pede depósito de R$ 500 antes da visita` |
| `status` | um de: `pendente`, `em_analise`, `revisada` | Sim | `pendente` |
| `criado_em` | data e hora | Sim | `2026-10-13T19:20:00` |

## Saída

Os campos gravados são os mesmos campos lidos (ver tabela acima).

## Erros

| Condição | O que retorna |
|---|---|
| O `vaga_id` não existe na tabela `vaga` | O banco recusa a gravação |
| O `estudante_id` não existe na tabela `estudante` | O banco recusa a gravação |
| Motivo fora da lista permitida | O banco recusa a gravação |