# CT23: autenticarProprietario

## Cabeçalho

| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT23 - autenticarProprietario |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Atende** | E13 |
| **Como chamar** | `POST /api/auth/login` |

**Autenticação:** utiliza o mesmo endpoint de login do CT7. Proprietários podem autenticar-se com e-mail pessoal; a validação de domínio institucional é específica para estudantes.

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| email | texto | sim | ana.souza@gmail.br |
| senha | texto | sim | MinhaSenha123 |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| token | texto | sim | eyJhbGciOiJIUzI1NiJ9.exemplo |
| usuarioId | inteiro | sim | 42 |
| perfil | um de: estudante, proprietario | sim | proprietario |

## Erros

| Quando | O que volta |
| :--- | :--- |
| E-mail ou senha incorretos | Erro `CREDENCIAIS_INVALIDAS` |
| Campo obrigatório não informado | Erro `DADOS_OBRIGATORIOS` |
| Falha interna durante a autenticação | Erro `ERRO_INTERNO` |