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

**1. Conversão de respostas em números (Escala 1 a 3)**
Cada resposta do questionário (CT15) e da regra da vaga (CT4) é convertida em um valor numérico para permitir o cálculo:
   **Horários (1 a 3):** `antes_das_22` = 1, `entre_22_e_0` = 2, `depois_da_0` = 3. (O mesmo vale para os horários de acordar).
   **Níveis (1 a 3):** `baixo` = 1, `medio` = 2, `alto` = 3 (Limpeza e Organização).
   **Frequências (1 a 3):** `nunca` = 1, `as_vezes` = 2, `frequentemente` = 3 (Visitas e Festas).
   **Booleano (Pet):** `false` = 1, `true` = 3.

**2. Definição de Pesos**
Cada uma das 10 dimensões tem um peso fixo. A soma de todos os pesos é exatamente 100:
   Horário de dormir (semana): 15
   Horário de dormir (fim de semana): 15
   Horário de acordar (semana): 10
   Horário de acordar (fim de semana): 10
   Limpeza: 10
   Organização: 10
   Tolerância a ruído: 10
   Frequência de visitas: 5
   Frequência de festas: 10
   Tem pet: 5

**3. Cálculo da Similaridade**
Para cada dimensão, o Backend compara a nota do estudante com a nota da vaga. A similaridade varia de **0.0 (totalmente diferente)** a **1.0 (totalmente igual)**.
   **Fórmula:** `similaridade = 1 - ( |nota_estudante - nota_vaga| / 2 )`
   *Nota:* O impacto retornado na API é `positivo` se a similaridade for maior ou igual a 0.7, e `negativo` se for menor.

**4. Nota Final**
A nota final é a soma ponderada das similaridades, arredondada para inteiro (0 a 100).
   **Fórmula:** `Nota = Soma(similaridade_dimensao * peso_dimensao)`

### Exemplo Prático de Cálculo
Suponha que o estudante e a vaga tenham as seguintes respostas em duas dimensões:

   **Dimensão 1 (Horário de dormir na semana - Peso 15):**
       Estudante respondeu `entre_22_e_0` (nota 2).
       Vaga exige `antes_das_22` (nota 1).
       Diferença: |2 - 1| = 1.
       Similaridade: 1 - (1 / 2) = 0.5.
       Pontuação da dimensão: 0.5 * 15 = **7.5 pontos**.

   **Dimensão 2 (Limpeza - Peso 10):**
       Estudante respondeu `alto` (nota 3).
       Vaga exige `alto` (nota 3).
       Diferença: |3 - 3| = 0.
       Similaridade: 1 - (0 / 2) = 1.0.
       Pontuação da dimensão: 1.0 * 10 = **10.0 pontos**.

Se o estudante e a vaga tiverem 100% de similaridade (nota 1.0) nas outras 8 dimensões, elas somarão 75 pontos.
   **Cálculo final:** 7.5 + 10.0 + 75 = 92.5.
   **Nota retornada na API:** `93` (arredondada).

## Erros

| Condição | O que retorna |
|---|---|
| Vaga não encontrada | Erro `VAGA_NAO_ENCONTRADA` |
| Estudante não encontrado | Erro `ESTUDANTE_NAO_ENCONTRADO` |
| Estudante ainda não respondeu o questionário | Erro `QUESTIONARIO_NAO_PREENCHIDO` |
| Falha interna durante a requisição | Erro `ERRO_INTERNO` |