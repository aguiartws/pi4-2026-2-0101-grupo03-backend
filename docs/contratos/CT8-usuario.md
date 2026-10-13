# CT8: usuario

## Cabeçalho

| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT8 - usuario |
| **Fronteira** | Banco |
| **Quem chama** | Backend |
| **Quem responde** | Banco |
| **Atende** | E4 |
| **Como chamar** | Consulta à tabela `usuario`, filtrando pelo e-mail institucional |

## Entrada

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| email | texto | sim | aluno@puc-campinas.edu.br |

## Saída

| Campo | Tipo | Obrigatório | Exemplo |
| :--- | :--- | :--- | :--- |
| id | inteiro | sim | 42 |
| email | texto | sim | aluno@puc-campinas.edu.br |
| senha_hash | texto | sim | $2b$12$exemploHash |
| perfil | um de: estudante, responsavel, admin | sim | estudante |

## Erros

| Quando | O que volta |
| :--- | :--- |
| Não existe usuário com o e-mail informado | A consulta não retorna registros, então o Backend informa credenciais inválidas ao Frontend por meio do CT7. |
| Ocorre uma falha na consulta ao banco | O Banco de Dados informa falha ao Backend, que retorna um erro de autenticação indisponível ao Frontend por meio do CT7. |