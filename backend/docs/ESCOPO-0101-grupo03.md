# Escopo do Projeto — Grupo 03 (PI IV 0101)

## Sumário

1. [Problema](#1-problema)
   - 1.1. [Evidência](#11-evidência)
2. [Objetivos](#2-objetivos)
   - 2.1. [Objetivo geral](#21-objetivo-geral)
   - 2.2. [Objetivos específicos](#22-objetivos-específicos)
   - 2.3. [Critério de sucesso](#23-critério-de-sucesso)
3. [O escopo: tabela dos entregáveis](#3-o-escopo-tabela-dos-entregáveis)
4. [Fora de escopo](#4-fora-de-escopo)
5. [A conta da viabilidade](#5-a-conta-da-viabilidade)
   - 5.1. [Passo 1 — Quantas semanas temos](#51-passo-1--quantas-semanas-temos)
   - 5.2. [Passo 2 — Horas por semana por integrante](#52-passo-2--horas-por-semana-por-integrante)
   - 5.3. [Passo 3 — Horas-pessoa disponíveis](#53-passo-3--horas-pessoa-disponíveis)
   - 5.4. [Passo 4 — Quanto o escopo custa](#54-passo-4--quanto-o-escopo-custa)
   - 5.5. [Passo 5 — Frase de fecho](#55-passo-5--frase-de-fecho)
6. [Onde está tudo](#6-onde-está-tudo)

---

## 1. Problema

Hoje, o estudante universitário que precisa morar perto da faculdade precisa encontrar uma vaga em república ou apartamento compartilhado que caiba no seu orçamento e fique perto do campus, mas as informações estão espalhadas em grupos de WhatsApp, Facebook, Instagram e sites de anúncios sem filtro, sem verificação e sem nenhuma informação sobre quem já mora na casa, o que causa semanas de procura, visitas a imóveis que não servem, risco de cair em golpe e a descoberta de conflitos de convivência só depois de já ter se mudado.

### 1.1. Evidência

**O que fizemos:** entre agosto e setembro de 2026 conversamos com 10 estudantes da PUC-Campinas que já procuraram moradia ou já moraram em república, e perguntamos como eles buscaram e quais as dificuldades. As conversas foram rápidas, presencialmente no campus. A partir das respostas, identificamos dois padrões recorrentes:

1. estudantes que pagaram mais caro em moradia individual só para não ter o trabalho de procurar república e dividir com desconhecidos;
2. estudantes que foram morar mais longe do campus porque não conseguiram encontrar, dentro do orçamento, opções de moradia compartilhada perto da faculdade.

**Três falas levantadas:**

> "O que me pegou não foi o preço, foi a pesquisa. Procurar república é um trabalho que ninguém tem tempo de fazer."

> "Se eu soubesse onde procurar e com quem dividir, já estaria morando a 10 minutos da faculdade."

> "Eu não preciso de mais anúncios, eu preciso saber com quem eu vou morar."

**O que ela mostra:** que o gargalo não é a falta de anúncio, e sim a falta de filtro e de informação sobre a convivência — os estudantes chegam a gastar semanas procurando e ainda assim decidem sem saber com quem vão morar.

---

## 2. Objetivos

### 2.1. Objetivo geral

Construir uma plataforma web de busca de moradia estudantil que reúna vagas cadastradas por repúblicas e moradias compartilhadas e mostre, para cada vaga, uma pontuação de compatibilidade com o perfil de convivência do estudante.

- **Ação:** Construir
- **Objeto:** Plataforma web de busca de moradia estudantil
- **Condição:** Reunir vagas cadastradas por repúblicas e moradias compartilhadas e mostrar, para cada vaga, uma pontuação de compatibilidade com o perfil de convivência do estudante

### 2.2. Objetivos específicos

- **OE1.** Permitir que o estudante e o responsável pela moradia cadastrem seus perfis e suas vagas.
- **OE2.** Possibilitar que o estudante encontre vagas por faixa de preço e proximidade do campus.
- **OE3.** Calcular e exibir a compatibilidade entre o perfil de convivência do estudante e a vaga conforme os filtros selecionados.
- **OE4.** Dar ao estudante meios de checar a confiabilidade da vaga e falar com o responsável por ela.

### 2.3. Critério de sucesso

Em dezembro, um estudante da PUC-Campinas cria seu perfil, responde o questionário de convivência, filtra vagas por preço e distância, abre uma vaga e vê a nota de compatibilidade com os critérios que a compõem, e solicita contato com o responsável pela própria plataforma, tudo em menos de 10 minutos, sem passar por grupo de WhatsApp.

---

## 3. O escopo: tabela dos entregáveis

| ID  | Entregável | Feito quando | Objetivo | Tamanho |
|-----|------------|--------------|----------|---------|
| E1  | Cadastro de perfil do estudante | O estudante preenche nome, e-mail, curso, faixa de orçamento e distância máxima do campus, salva, e o perfil aparece na tela "Meu perfil" com os dados que ele digitou. | OE1 | P |
| E2  | Cadastro de vaga pela república | O responsável cadastra uma vaga com endereço, valor do aluguel, número de moradores e regras da casa, salva, e a vaga aparece na lista de vagas ativas. | OE1 | M |
| E3  | Painel "Minhas vagas" do responsável | O responsável abre o painel, vê todas as vagas que cadastrou, edita o valor de uma delas, e o novo valor aparece na página pública da vaga. | OE1 | M |
| E4  | Login com e-mail institucional | O usuário entra com e-mail e senha; senha errada mostra mensagem de erro; e-mail fora do domínio institucional não conclui o cadastro. | OE1 | P |
| E5  | Busca com filtros de preço e distância | O estudante define preço máximo e distância máxima até o campus, aplica os filtros, e a lista mostra apenas as vagas que respeitam os dois limites ao mesmo tempo. | OE2 | M |
| E6  | Lista e mapa de vagas | O estudante abre a tela de vagas e vê a lista com endereço e valor, e o mapa mostra um marcador para cada vaga da lista. | OE2 | M |
| E7  | Favoritos | O estudante clica em "salvar" em uma vaga, e ela aparece na tela "Favoritos" na próxima vez que ele entrar. | OE2 | P |
| E8  | Questionário de convivência | O estudante responde 10 perguntas de hábitos (horário de dormir e acordar, limpeza, tolerância a ruído, visitas, pets) e as respostas ficam salvas no perfil dele. | OE3 | P |
| E9  | Pontuação de compatibilidade estudante × vaga | O estudante abre uma vaga e vê uma nota de 0 a 100 e a lista dos critérios que mais pesaram na nota; ao alterar uma resposta do questionário, a nota da mesma vaga muda. | OE3 | G |
| E10 | Notificação por e-mail com link para o WhatsApp | O estudante clica em "Tenho interesse" em uma vaga, e ambos (estudante e responsável) recebem um e-mail com o nome e o e-mail um do outro e um link `wa.me` que abre a conversa no WhatsApp deles. | OE4 | M |
| E11 | Denúncia de anúncio | O estudante clica em "denunciar" em uma vaga, escolhe um motivo e envia, e a vaga sai da lista pública até a moderação revisar. | OE4 | P |

---

## 4. Fora de escopo

| O que fica de fora? | Por quê? |
|---------------------|----------|
| Aplicativo mobile | O front será web e responsivo e abre no navegador do celular. Manter duas bases de código dobraria o trabalho de cada tela sem mudar o que o estudante consegue fazer. |
| Algoritmo de IA treinado com machine learning | Não temos histórico de matches para o modelo aprender nem como validar se ele acerta. A pontuação será calculada por regras ponderadas, que dá para explicar, testar e ajustar à mão. |
| Contrato de locação gerado e assinado dentro da plataforma | Envolve intermediação imobiliária e certificado digital, o que traz risco jurídico que não conseguiríamos validar em 12 semanas. O contrato fica fora do sistema. |
| Pagamento de aluguel e caução pela plataforma | Depende de integração com meio de pagamento e cria obrigações fiscais e de segurança de dados que fogem do problema escolhido, que é encontrar a moradia, não cobrar por ela. |
| Verificação presencial dos imóveis pela nossa equipe | Não temos como ir até os imóveis nem garantir cobertura em Campinas. Ficam a verificação por e-mail institucional e a denúncia de anúncio. |
| Chat interno | Trocar mensagens dentro da plataforma exigiria tempo real, notificações, histórico e moderação, o que não é o problema central, que é conectar estudante e vaga. O contato fica por e-mail e por meio de telefone, e o chat pode ser desenvolvido em uma versão futura. |

---

## 5. A conta da viabilidade

### 5.1. Passo 1 — Quantas semanas temos

Da data desta entrega (15/09/2026) até a entrega final do projeto, descontando os feriados e recessos (12/10, 15 a 17/10, 02/11, 20 e 21/11 e 08/12): **12 semanas**.

### 5.2. Passo 2 — Horas por semana por integrante

| Integrante   | Horas por semana |
|--------------|-----------------:|
| Ana Carolina | 10 |
| Ana Clara    | 12 |
| Brenda       | 10 |
| Caio         | 10 |
| Luis         | 10 |
| Maria Julia  | 10 |
| **Total do grupo, por semana** | **62** |

### 5.3. Passo 3 — Horas-pessoa disponíveis

62 horas por semana × 12 semanas = **744 horas-pessoa**

### 5.4. Passo 4 — Quanto o escopo custa

| Tamanho | Quantos itens | Horas |
|---------|--------------:|------:|
| P       | 5  | 40 |
| M       | 5  | 100 |
| G       | 1  | 40 |
| **Total** | **11 itens** | **180 horas** |

### 5.5. Passo 5 — Frase de fecho

Temos 744 horas-pessoa e o escopo pede 180. Cabe, com folga de 564 horas para imprevisto, integração entre as partes, testes e a documentação das próximas entregas.

---

## 6. Onde está tudo

- **Repositório:** <https://github.com/aguiartws/pi4-2026-2-0101-grupo03>
- **Backend:** <https://github.com/aguiartws/pi4-2026-2-0101-grupo03-backend>
- **Frontend:** <https://github.com/aguiartws/pi4-2026-2-0101-grupo03-frontend>
- **Card no OpenProject:** <https://srv1889234.hstgr.cloud/projects/pi-iv-0101-g3/work_packages/157/activity>

**Integrantes:**

| Nome | GitHub |
|------|--------|
| Ana Carolina Ferreira Alves Barcaro | [@carolbarcaro](https://github.com/carolbarcaro) |
| Ana Clara Coelho Chaves | [@anachaves26](https://github.com/anachaves26) |
| Brenda Maia Bergamasco | [@bMaia07](https://github.com/bMaia07) |
| Caio Cantarin Marques | [@caio0409](https://github.com/caio0409) |
| Luis Filippi Aguiar da Rocha | [@aguiartws](https://github.com/aguiartws) |
| Maria Júlia Hofstetter Trevisan Pereira | [@m7julia](https://github.com/m7julia) |