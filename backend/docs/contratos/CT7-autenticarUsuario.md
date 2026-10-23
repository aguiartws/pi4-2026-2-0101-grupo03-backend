# CT7: autenticarUsuario

## Cabeçalho

| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT7 - autenticarUsuario |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E4 |
| **Como chamar** | `POST /api/auth/login` |

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| email | texto | sim | aluno@puc-campinas.edu.br |
| senha | texto | sim | MinhaSenha123 |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| token | texto | sim | eyJhbGciOiJIUzI1NiJ9.exemplo |
| usuarioId | inteiro | sim | 42 |
| perfil | um de: estudante, responsavel, admin | sim | estudante |

## Erros

| Quando | O que volta |
| :--- | :--- |
| E-mail fora do domínio institucional permitido | Erro `EMAIL_INSTITUCIONAL_INVALIDO` |
| E-mail ou senha incorretos | Erro `CREDENCIAIS_INVALIDAS` |
| Campo obrigatório não informado | Erro `DADOS_OBRIGATORIOS` |
| Falha interna durante a autenticação | Erro `ERRO_INTERNO` |