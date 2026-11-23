# Escopo do Projeto — FindUHouse | Grupo 03 (PI IV 0101)

## 1. Problema

Estudantes universitários que precisam morar perto da faculdade encontram dificuldade para localizar repúblicas, kitnets e apartamentos compartilhados que atendam ao orçamento e às suas preferências. Os anúncios ficam espalhados em grupos de WhatsApp, redes sociais e sites de classificados, frequentemente sem filtros suficientes e sem informações sobre a convivência. Isso torna a busca demorada e dificulta avaliar se a moradia combina com a rotina do estudante.

### 1.1. Evidência

Entre agosto e setembro de 2026, o grupo conversou com 10 estudantes da PUC-Campinas que já procuraram moradia ou moraram em república. As conversas foram presenciais e rápidas, realizadas no campus. Os relatos apontaram dificuldades para encontrar opções dentro do orçamento e próximas à universidade, além da falta de informações sobre possíveis moradores e regras de convivência.

As falas e os registros originais da pesquisa devem ser consultados na documentação de pesquisa do grupo. Este documento não acrescenta citações literais que não estejam registradas na fonte original.

## 2. Objetivos

### 2.1. Objetivo geral

Construir uma plataforma web de busca de moradia estudantil que reúna vagas cadastradas por proprietários e permita que estudantes consultem informações das moradias, utilizem filtros e visualizem informações de compatibilidade de convivência.

### 2.2. Objetivos específicos

- **OE1.** Permitir o cadastro e a autenticação de estudantes e proprietários.
- **OE2.** Permitir que proprietários cadastrem, consultem e editem vagas.
- **OE3.** Permitir que estudantes busquem vagas, apliquem filtros e consultem sua localização.
- **OE4.** Permitir que estudantes salvem vagas favoritas e respondam ao questionário de convivência.
- **OE5.** Apresentar informações de compatibilidade e permitir o registro de interesse em uma vaga.

## 3. Entregáveis

A tabela abaixo separa entregáveis implementados nesta versão de itens removidos. Os identificadores históricos não são reutilizados para funcionalidades de significado diferente.

| ID | Entregável | Critério de aceite resumido | Situação |
|---|---|---|---|
| E1 | Cadastro de perfil do estudante | Estudante cadastra seus dados utilizando e-mail institucional. | Implementado |
| E2 | Cadastro de vaga pelo proprietário | Proprietário cadastra uma vaga com informações da moradia. | Implementado |
| E3 | Painel “Minhas vagas” | Proprietário consulta e gerencia as vagas cadastradas. | Implementado |
| E4 | Login do estudante | Estudante autentica com e-mail institucional e senha. | Implementado |
| E5 | Busca com filtros | Estudante consulta vagas aplicando filtros disponíveis. | Implementado |
| E6 | Lista e mapa de vagas | Estudante consulta as vagas em lista e visualização geográfica. | Implementado; conferir disponibilidade de dados geográficos no ambiente de execução |
| E7 | Favoritos | Estudante salva e consulta vagas favoritas. | Implementado |
| E8 | Questionário de convivência | Estudante registra suas respostas sobre hábitos e preferências. | Implementado |
| E9 | Compatibilidade estudante × vaga | Sistema apresenta informações/pontuação de compatibilidade com base nos dados de convivência. | Implementado; conferir os critérios exibidos na interface final |
| E10 | Solicitação de interesse e notificação | Estudante registra interesse em uma vaga e o fluxo de notificação é acionado. | Implementado; depende da configuração do serviço de e-mail |
| E11 | Denúncia de anúncio | Funcionalidade de denúncia e moderação de anúncios. | **Removido do escopo** |
| E12 | Cadastro de perfil do proprietário | Proprietário cadastra seu perfil, podendo utilizar e-mail pessoal. | Implementado |
| E13 | Login do proprietário | Proprietário autentica para acessar o gerenciamento de suas moradias. | Implementado |

> **Nota sobre identificadores:** E11 permanece registrado apenas como item removido, para preservar o histórico e evitar reutilização de identificador. Cadastro e login de proprietário são identificados como E12 e E13 nesta revisão. Atualize as referências cruzadas nos demais documentos para utilizar esses identificadores.

## 4. Fora de escopo

- Aplicativo mobile nativo; a aplicação desta versão é web.
- Algoritmo de inteligência artificial treinado por machine learning; a compatibilidade é baseada na lógica implementada no projeto.
- Geração e assinatura de contrato de locação dentro da plataforma.
- Pagamento de aluguel ou caução pela plataforma.
- Verificação presencial dos imóveis pela equipe do projeto.
- Chat interno entre estudantes e proprietários.
- Denúncia e moderação de anúncios nesta versão (item E11 removido).

## 5. Viabilidade

O planejamento de horas e semanas deve refletir o cronograma efetivamente aprovado pelo grupo. Os valores da estimativa inicial não foram reproduzidos nesta revisão porque precisam ser conferidos com o planejamento atualizado e com o quadro do OpenProject.

## 6. Repositórios e acompanhamento

- **Frontend:** https://github.com/aguiartws/pi4-2026-2-0101-grupo03-frontend
- **Backend:** https://github.com/aguiartws/pi4-2026-2-0101-grupo03-backend
- **OpenProject:** https://srv1889234.hstgr.cloud/projects/pi-iv-0101-g3/work_packages/157/activity

Os contratos técnicos correspondentes estão em `backend/docs/contratos/`. As decisões de arquitetura estão registradas em `backend/docs/adr/`.
