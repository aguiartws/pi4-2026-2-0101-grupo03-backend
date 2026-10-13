# CT8: usuario

## Cabeçalho

| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT8 - usuario |
| **Fronteira** | Banco |
| **Quem chama** | Backend |
| **Quem responde** | Banco |
| **Atende** | E1, E4, E10 |
| **Como chamar** | Tabela usuario, chave id; consulta por email (E4) ou por id (E10); gravação no cadastro (E1) |

## Entrada e Saída (os campos gravados e lidos são os mesmos)

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| id | inteiro | sim | 42 |
| nome | texto | sim | Ana Souza |
| email | texto | sim | aluno@puc-campinas.edu.br |
| telefone | texto | sim | +5519999990000 |
| senha_hash | texto | sim | $2b$12$exemploHash |
| perfil | um de: estudante, responsavel, admin | sim | estudante |

## Erros

| Quando | O que volta |
| :--- | :--- |
| Não existe usuário com o e-mail informado | A consulta não retorna registros, então o Backend informa credenciais inválidas ao Frontend por meio do CT7. |
| Ocorre uma falha na consulta ao banco | O Banco de Dados informa falha ao Backend, que retorna um erro de autenticação indisponível ao Frontend por meio do CT7. |
| E-mail duplicado | O banco recusa a gravação |