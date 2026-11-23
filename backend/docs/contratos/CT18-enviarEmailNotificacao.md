# CT18: enviarEmailNotificacao

## Cabeçalho

| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT18 - enviarEmailNotificacao |
| **Fronteira** | Externo |
| **Quem chama** | Backend |
| **Quem responde** | Serviço de E-mail (externo) |
| **Atende** | E10 - Notificação por e-mail com link para o WhatsApp |
| **Como chamar** | serviço SMTP configurado no backend (`JavaMailSender`) |

**Observação:** o backend envia dois e-mails distintos: um ao estudante com os dados de contato do proprietário e outro ao proprietário com os dados de contato do estudante. A implementação utiliza SMTP; não há integração direta com a API SendGrid no código revisado.

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| destinatario | texto | Sim | estudante@puccampinas.edu.br |
| nomeDestinatario | texto | Sim | João Silva |
| assunto | texto | Sim | Novo interesse na sua vaga |
| corpo | texto | Sim | Olá João, o responsável pela vaga é Maria. Contato: maria@email.com. WhatsApp: https://wa.me/5519999990000 |
| nomeContato | texto | Sim | Maria Souza |
| emailContato | texto | Sim | maria@email.com |
| linkWhatsApp | texto | Sim | https://wa.me/5519999990000 |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| resultado | texto | Sim | Envio executado pelo serviço SMTP |

## Erros

| Condição | O que retorna |
| :--- | :--- |
| E-mail de destinatário inválido | O serviço recusa. O Backend registra a falha e retorna Erro `FALHA_ENVIO_EMAIL` no CT17 |
| Serviço indisponível | O serviço retorna erro. O Backend registra a falha e não tenta de novo |