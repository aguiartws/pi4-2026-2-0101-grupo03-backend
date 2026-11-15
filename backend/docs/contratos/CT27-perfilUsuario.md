# CT27: perfilUsuario

## Cabeçalho

| Linha | Valor |
| :--- | :--- |
| **ID e nome** | CT27 - perfilUsuario |
| **Fronteira** | HTTP |
| **Quem chama** | Frontend |
| **Quem responde** | Backend |
| **Como chamar** | GET ou PUT `/api/usuario/perfil` com Bearer; fotos em POST `/api/alunos/{id}/foto` ou `/api/proprietarios/{id}/foto` |

## GET `/api/usuario/perfil`

### Saída

| Campo | Tipo | Obrigatório |
| :--- | :--- | :--- |
| id | inteiro | sim |
| tipoUsuario | `aluno` ou `proprietario` | sim |
| nome | texto | sim |
| email | texto | sim |
| telefone | texto | sim |
| cidade | texto ou nulo | não |
| curso | texto ou nulo | não |
| fotoPerfilUrl | texto ou nulo | não |

## PUT `/api/usuario/perfil`

Recebe `multipart/form-data` autenticado. `nome`, `email` e `telefone` são
obrigatórios. Também aceita `tipoUsuario`, `cidade`, `curso`, `novaSenha`,
`removerFoto` e a parte opcional `fotoPerfil`.

Retorna os mesmos campos do GET com os valores atualizados.

## Fotos de perfil

`POST /api/alunos/{id}/foto` e `POST /api/proprietarios/{id}/foto` recebem a
parte multipart `foto` e retornam `{ "fotoUrl": "..." }`. O usuário só pode
alterar a própria foto e o perfil do token deve corresponder à rota.

## Erros

| Quando | Código |
| :--- | :--- |
| Campo obrigatório, arquivo inválido ou limite de tamanho | `DADOS_OBRIGATORIOS` ou `ARQUIVO_INVALIDO` |
| Perfil ou usuário sem permissão | `ACESSO_NEGADO` |
| E-mail institucional inválido | `EMAIL_INSTITUCIONAL_INVALIDO` |
| E-mail já utilizado | `EMAIL_JA_EXISTE` |
| Cloudinary não configurado ou indisponível | `FALHA_UPLOAD_IMAGEM` (HTTP 502) |
