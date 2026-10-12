# CT17: solicitarInteresse

## Cabeçalho

| Linha | Valor |
|---|---|
| **ID e nome** | CT17 - solicitarInteresse |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E10 - Notificação por e-mail com link para o WhatsApp |
| **Como chamar** | `POST /api/vagas/{vagaId}/interesse` |

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `vagaId` | inteiro | Sim | `17` |
| `estudanteId` | inteiro | Sim | `42` |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `sucesso` | booleano | Sim | `true` |
| `emailEnviado` | booleano | Sim | `true` |
| `mensagem` | texto | Sim | `Interesse registrado. Verifique seu e-mail.` |

## Erros

| Condição | O que retorna |
|---|---|
| Vaga não encontrada | Erro `VAGA_NAO_ENCONTRADA` |
| Estudante não encontrado | Erro `ESTUDANTE_NAO_ENCONTRADO` |
| Responsável da vaga sem e-mail cadastrado | Erro `RESPONSAVEL_SEM_EMAIL` |
| Interesse já registrado por este estudante | Erro `INTERESSE_JA_REGISTRADO` |
| Falha ao enviar e-mail | Erro `FALHA_ENVIO_EMAIL` |
| Falha interna durante a requisição | Erro `ERRO_INTERNO` |