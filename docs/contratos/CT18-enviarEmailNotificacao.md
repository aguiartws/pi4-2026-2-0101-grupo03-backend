# CT18: enviarEmailNotificacao

## Cabeçalho

| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT18 - enviarEmailNotificacao |
| **Fronteira** | Externo |
| **Quem chama** | Backend |
| **Quem responde** | Serviço de E-mail (externo) |
| **Atende** | E10 |
| **Como chamar** | Conforme documentação do provedor SMTP configurado: https://exemplo.com/docs/smtp |

**Observação:** esta operação é chamada duas vezes pelo Backend — uma para o estudante, uma para o responsável — com conteúdos diferentes.

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| destinatario | texto | sim | estudante@puccampinas.edu.br |
| nomeDestinatario | texto | sim | João Silva |
| assunto | texto | sim | Novo interesse na sua vaga |
| corpo | texto | sim | Olá João, o responsável pela vaga é Maria. Contato: maria@email.com. WhatsApp: https://wa.me/5519999990000 |
| nomeContato | texto | sim | Maria Souza |
| emailContato | texto | sim | maria@email.com |
| linkWhatsApp | texto | sim | https://wa.me/5519999990000 |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| mensagemId | texto | sim | msg_8f2a |
| status | texto | sim | aceito |

## Erros

| Condição | O que retorna |
| :--- | :--- |
| E-mail de destinatário inválido | O serviço recusa. O Backend registra a falha e retorna Erro FALHA_ENVIO_EMAIL no CT17 |
| Serviço indisponível | O serviço retorna erro. O Backend registra a falha e não tenta de novo |