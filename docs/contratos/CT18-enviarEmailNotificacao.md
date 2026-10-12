# CT18: enviarEmailNotificacao

## Cabeçalho

| Linha | Valor |
|---|---|
| **ID e nome** | CT18 - enviarEmailNotificacao |
| **Fronteira** | Externo |
| **Quem chama** | Backend |
| **Quem responde** | Serviço de E-mail (externo) |
| **Atende** | E10 - Notificação por e-mail com link para o WhatsApp |
| **Como chamar** | Conforme documentação do provedor SMTP configurado |

**Observação:** esta operação é chamada duas vezes pelo Backend — uma para o estudante, uma para o responsável — com conteúdos diferentes.

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `destinatario` | texto | Sim | `estudante@puccampinas.edu.br` |
| `nomeDestinatario` | texto | Sim | `João Silva` |
| `assunto` | texto | Sim | `Novo interesse na sua vaga` |
| `corpo` | texto | Sim | `Olá João, o responsável pela vaga é Maria. Contato: maria@email.com. WhatsApp: https://wa.me/5519999990000` |
| `nomeContato` | texto | Sim | `Maria Souza` |
| `emailContato` | texto | Sim | `maria@email.com` |
| `linkWhatsApp` | texto | Sim | `https://wa.me/5519999990000` |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
|---|---|---|---|
| `mensagemId` | texto | Sim | `msg_8f2a` |
| `status` | texto | Sim | `aceito` |

## Erros

| Condição | O que retorna |
|---|---|
| E-mail de destinatário inválido | O serviço recusa. O Backend registra a falha e retorna Erro `FALHA_ENVIO_EMAIL` no CT17 |
| Serviço indisponível | O serviço retorna erro. O Backend registra a falha e não tenta de novo |