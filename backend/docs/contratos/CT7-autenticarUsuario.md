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
| email | texto | sim | aluno@puccampinas.edu.br |
| senha | texto | sim | MinhaSenha123 |
| lembrar | booleano | não | false |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| token | texto | sim | eyJhbGciOiJIUzI1NiJ9.exemplo |
| usuarioId | inteiro | sim | 42 |
| perfil | um de: estudante, proprietario | sim | estudante |

**Observação:** os perfis funcionais do sistema são `estudante` e `proprietario`. Existe uma constante residual `ADMIN` no enum do DTO de autenticação, mas ela não corresponde a um perfil suportado pela entidade de usuário nem pelo banco de dados.

A validação de domínio institucional é aplicada ao cadastro/login de estudante; proprietário pode utilizar e-mail pessoal.

## Erros

| Quando | O que volta |
| :--- | :--- |
| E-mail fora do domínio institucional permitido | Erro `EMAIL_INSTITUCIONAL_INVALIDO` |
| E-mail ou senha incorretos | Erro `CREDENCIAIS_INVALIDAS` |
| Campo obrigatório não informado | Erro `DADOS_OBRIGATORIOS` |
| Falha interna durante a autenticação | Erro `ERRO_INTERNO` |