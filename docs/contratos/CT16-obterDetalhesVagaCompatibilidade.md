# CT16: obterDetalhesVagaCompatibilidade

## Cabeçalho

| Linha | Valor |
|---|---|
| **ID e nome** | CT16 - obterDetalhesVagaCompatibilidade |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E9 - Pontuação de compatibilidade |
| **Como chamar** | `GET /api/vagas/{vagaId}/compatibilidade?estudanteId={estudanteId}` |

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `vagaId` | inteiro | Sim | `17` |
| `estudanteId` | inteiro | Sim | `42` |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `vagaId` | inteiro | Sim | `17` |
| `nota` | inteiro | Sim | `85` |
| `dimensoes` | lista de (objeto Dimensao) | Sim | Ver tabela abaixo |

Cada objeto `Dimensao` contém os seguintes campos:

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `nome` | texto | Sim | `Horário de dormir` |
| `peso` | inteiro | Sim | `15` |
| `similaridade` | decimal | Sim | `0.92` |
| `impacto` | um de: `positivo`, `negativo` | Sim | `positivo` |

**Cálculo:** o Backend transforma as respostas do estudante em um vetor de 10 posições (uma por pergunta) e as regras da vaga em outro vetor. A similaridade de cada dimensão é calculada por similaridade de cosseno. A nota final é a soma ponderada das similaridades, arredondada para inteiro entre 0 e 100. O impacto é `positivo` quando similaridade >= 0.7 e `negativo` caso contrário.

## Erros

| Condição | O que retorna |
|---|---|
| Vaga não encontrada | Erro `VAGA_NAO_ENCONTRADA` |
| Estudante não encontrado | Erro `ESTUDANTE_NAO_ENCONTRADO` |
| Estudante ainda não respondeu o questionário | Erro `QUESTIONARIO_NAO_PREENCHIDO` |
| Falha interna durante a requisição | Erro `ERRO_INTERNO` |