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
| `dimensoes` | lista de objeto Dimensao | Sim | Ver tabela abaixo |

Cada objeto `Dimensao` contém os seguintes campos:

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `nome` | texto | Sim | `Horário de dormir` |
| `peso` | inteiro | Sim | `15` |
| `similaridade` | decimal | Sim | `0.92` |
| `impacto` | um de: `positivo`, `negativo` | Sim | `positivo` |

## Regra de Cálculo da Compatibilidade

O cálculo da nota de compatibilidade (0 a 100) é feito pelo Backend em 4 etapas:

**1. Comparação das respostas**
Cada resposta do questionário (CT15) é comparada diretamente com a resposta equivalente da vaga (CT3). Como todas as respostas são booleanas, a similaridade é `1.0` quando são iguais e `0.0` quando são diferentes.

**2. Definição de Pesos**
Cada uma das 10 dimensões tem um peso fixo. A soma de todos os pesos é exatamente 100:
   Aceita barulho depois das 22h: 15
   Fuma: 15
   Aceita animais: 10
   Gosta de receber visitas: 10
   Gosta de cozinhar: 10
   Estuda em casa: 10
   Aceita dividir tarefas: 10
   Aceita dividir quarto: 5
   Aceita festas: 10
   É comunicativo: 5

**3. Cálculo da Similaridade**
O impacto retornado na API é `positivo` quando as respostas são iguais e `negativo` quando são diferentes.

**4. Nota Final**
A nota final é a soma ponderada das similaridades, arredondada para inteiro (0 a 100).
   **Fórmula:** `Nota = Soma(similaridade_dimensao * peso_dimensao)`

### Exemplo Prático de Cálculo
Suponha que o estudante e a vaga tenham as seguintes respostas em duas dimensões:

   **Dimensão 1 (Horário de dormir na semana - Peso 15):**
       Estudante respondeu `true`.
       Vaga respondeu `false`.
       Similaridade: 0.0.
       Pontuação da dimensão: 0.0 * 15 = **0 pontos**.

   **Dimensão 2 (Limpeza - Peso 10):**
       Estudante respondeu `true`.
       Vaga respondeu `true`.
       Similaridade: 1.0.
       Pontuação da dimensão: 1.0 * 10 = **10.0 pontos**.

Se o estudante e a vaga tiverem 100% de similaridade nas outras 8 dimensões, elas somarão 75 pontos.
   **Cálculo final:** 0 + 10.0 + 75 = 85.
   **Nota retornada na API:** `85`.

## Erros

| Condição | O que retorna |
|---|---|
| Vaga não encontrada | Erro `VAGA_NAO_ENCONTRADA` |
| Estudante não encontrado | Erro `ESTUDANTE_NAO_ENCONTRADO` |
| Estudante ainda não respondeu o questionário | Erro `QUESTIONARIO_NAO_PREENCHIDO` |
| Falha interna durante a requisição | Erro `ERRO_INTERNO` |