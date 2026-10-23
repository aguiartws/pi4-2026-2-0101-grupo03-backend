# CT12: favorito

## Cabeçalho

| Linha | Valor |
|---|---|
| **ID e nome** | CT12 - favorito |
| **Fronteira** | Banco de Dados |
| **Quem chama** | Backend |
| **Quem responde** | Banco de Dados |
| **Atende** | E7 - Favoritos |
| **Como chamar** | Operações de inserção e consulta na tabela `favorito`, com consulta à tabela `vaga` quando necessário |

## Entrada

A entrada varia de acordo com a operação realizada.

**Operação 1 - Salvar favorito**

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `estudante_id` | inteiro | Sim | `42` |
| `vaga_id` | inteiro | Sim | `15` |

**Operação 2 - Consultar favoritos**

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `estudante_id` | inteiro | Sim | `42` |

## Saída

**Operação 1 - Salvar favorito**

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `estudante_id` | inteiro | Sim | `42` |
| `vaga_id` | inteiro | Sim | `15` |

**Operação 2 - Consultar favoritos**

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `favoritos` | lista de objeto Vaga | Sim | Lista de vagas favoritadas pelo estudante |

Cada objeto `vaga` pode conter:

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `id` | inteiro | Sim | `15` |
| `titulo` | texto | Sim | `Kitnet próxima à universidade` |
| `endereco` | texto | Sim | `Rua das Flores, 100, Campinas` |
| `valor_aluguel` | decimal | Sim | `1200.00` |

## Erros

| Condição | O que retorna |
|---|---|
| Estudante ou vaga não encontrado durante a gravação | Operação não realizada |
| Vínculo entre estudante e vaga já existe | Não realiza nova inserção |
| Falha ao inserir ou consultar os registros | Erro de banco de dados retornado ao Backend |