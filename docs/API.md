# Unifor Books API

> **Versão:** 1.0.0  
> **Base URL:** configurada em `RetrofitClient`  
> **Autenticação:** Bearer JWT — todas as rotas protegidas exigem o header `Authorization: Bearer <accessToken>`

---

## Índice

- [Auth](#auth)
- [Users](#users)
- [Books](#books)
- [Exemplares](#exemplares)
- [Empréstimos](#empréstimos)
- [Multas](#multas)
- [Engajamento](#engajamento)
  - [Comentários](#comentários)
  - [Likes](#likes)
  - [Avaliações (Ratings)](#avaliações-ratings)
- [Modelos de Dados](#modelos-de-dados)

---

## Auth

### `POST /auth/login`
Autentica o usuário e retorna os tokens de acesso.

**Body:**
```json
{
  "matricula": "string",
  "senha": "string"
}
```

**Resposta 200:**
```json
{
  "accessToken": "string",
  "refreshToken": "string",
  "usuario": {
    "id": "string",
    "matricula": "string",
    "nome": "string",
    "email": "string",
    "role": "ALUNO | ADMIN"
  }
}
```

**Resposta 401:** Credenciais inválidas.

---

### `POST /auth/refresh`
Renova o `accessToken` usando o `refreshToken`.

**Body:**
```json
{
  "refreshToken": "string"
}
```

**Resposta 200:**
```json
{
  "accessToken": "string"
}
```

**Resposta 401:** Refresh token inválido ou expirado.

---

### `POST /auth/logout` 🔒
Invalida o refresh token do usuário.

**Body:**
```json
{
  "refreshToken": "string"
}
```

**Resposta 200:**
```json
{
  "message": "string"
}
```

---

## Users

### `POST /users` 🔒
Cria um novo usuário (admin).

**Body:**
```json
{
  "matricula": "string",
  "nome": "string",
  "email": "string",
  "senha": "string (min 6 chars)",
  "role": "ALUNO | ADMIN (default: ALUNO)"
}
```

**Resposta 201:** Objeto `User` criado.  
**Resposta 409:** Matrícula ou e-mail já cadastrado.

---

### `GET /users` 🔒
Lista todos os usuários.

**Resposta 200:**
```json
{
  "users": [ ...User ]
}
```

---

### `GET /users/{id}` 🔒
Busca um usuário pelo ID.

**Resposta 200:**
```json
{
  "user": { ...User }
}
```

**Resposta 404:** Usuário não encontrado.

---

## Books

### `GET /books` 🔒
Lista os livros com suporte a filtros, paginação e ordenação.

**Query params:**

| Parâmetro  | Tipo   | Obrigatório | Descrição                              |
|------------|--------|-------------|----------------------------------------|
| `titulo`   | string | Não         | Filtro por título                      |
| `autor`    | string | Não         | Filtro por autor                       |
| `categoria`| string | Não         | Filtro por categoria                   |
| `orderBy`  | string | Não         | `avaliacao`, `titulo` ou `criadoEm`    |
| `page`     | number | Não         | Página atual (default: 1)              |
| `limit`    | number | Não         | Itens por página (default: 20)         |

**Resposta 200:**
```json
{
  "data": [ ...Book ],
  "total": 100,
  "page": 1,
  "limit": 20
}
```

---

### `POST /books` 🔒
Cria um novo livro.

**Body:**
```json
{
  "titulo": "string",
  "autor": "string",
  "isbn": "string",
  "categoria": "string",
  "sinopse": "string (opcional)",
  "capaUrl": "string (opcional)",
  "anoPublicacao": 2024,
  "editora": "string (opcional)",
  "idioma": "string (opcional)",
  "paginas": 432
}
```

**Resposta 201:** Objeto `Book` criado.  
**Resposta 409:** ISBN já cadastrado.

---

### `GET /books/{id}` 🔒
Retorna os detalhes de um livro, incluindo seus exemplares.

**Resposta 200:** Objeto `Book` com campo `exemplares: Exemplar[]`.  
**Resposta 404:** Livro não encontrado.

---

### `PUT /books/{id}` 🔒
Atualiza os dados de um livro.

**Body:** Campos opcionais do livro (mesmo schema do POST, sem `isbn`).

**Resposta 200:** Objeto `Book` atualizado.  
**Resposta 404:** Livro não encontrado.

---

### `DELETE /books/{id}` 🔒
Remove um livro.

**Resposta 200:** Objeto `Book` removido.  
**Resposta 404:** Livro não encontrado.

---

## Exemplares

### `POST /books/{id}/exemplares` 🔒
Adiciona um exemplar físico a um livro.

**Body:**
```json
{
  "numeroTombo": "string"
}
```

**Resposta 201:** Objeto `Exemplar` criado.  
**Resposta 409:** Número de tombo já existe.

---

### `PATCH /books/exemplares/{exemplarId}/status` 🔒
Altera o status de um exemplar.

**Body:**
```json
{
  "status": "DISPONIVEL | EMPRESTADO | INDISPONIVEL"
}
```

**Resposta 200:** Objeto `Exemplar` atualizado.  
**Resposta 404:** Exemplar não encontrado.

---

### `PATCH /books/exemplares/{exemplarId}` 🔒
Atualiza dados de um exemplar.

**Resposta 200:** Exemplar atualizado.

---

### `DELETE /books/{id}/exemplares/{exemplarId}` 🔒
Remove um exemplar de um livro.

**Resposta 200:** `{ "message": "string" }`  
**Resposta 404:** Livro ou exemplar não encontrado.  
**Resposta 409:** Exemplar está em uso (emprestado).

---

## Empréstimos

### `POST /emprestimos/solicitar` 🔒
Solicita o empréstimo de um livro pelo aluno.

**Body:**
```json
{
  "livroId": "string"
}
```

**Resposta 201** — Empréstimo criado com status `PENDENTE`:
```json
{
  "message": "string",
  "emprestimo": { ...Emprestimo }
}
```

**Resposta 202** — Livro indisponível, aluno entrou na fila de espera:
```json
{
  "message": "string",
  "fila": {
    "id": "string",
    "usuarioId": "string",
    "livroId": "string",
    "posicao": 3,
    "status": "string"
  }
}
```

**Resposta 400:** Dados inválidos.  
**Resposta 409:** Aluno já possui empréstimo ativo deste livro.

---

### `GET /emprestimos/meus` 🔒
Retorna os empréstimos e a fila de espera do aluno autenticado.

**Resposta 200:**
```json
{
  "emprestimos": [ ...Emprestimo ],
  "filaEspera": [ ...FilaEspera ]
}
```

---

### `GET /emprestimos` 🔒
Lista todos os empréstimos (admin).

---

### `POST /emprestimos/{id}/renovar` 🔒
Solicita a renovação de um empréstimo.

**Resposta 200:** `{ "message": "string", "emprestimo": { ...Emprestimo } }`  
**Resposta 400:** Renovação não permitida (prazo excedido, multa pendente, etc.).

---

### `PATCH /emprestimos/{id}/aprovar` 🔒
Aprova um empréstimo pendente (admin).

**Resposta 200:** `{ "message": "string", "emprestimo": { ...Emprestimo } }`

---

### `PATCH /emprestimos/{id}/negar` 🔒
Nega um empréstimo pendente (admin).

**Body:**
```json
{
  "motivoNegacao": "string"
}
```

**Resposta 200:** `{ "message": "string", "emprestimo": { ...Emprestimo } }`

---

### `PATCH /emprestimos/{id}/devolver` 🔒
Registra a solicitação de devolução pelo aluno.

**Resposta 200:** `{ "message": "string", "emprestimo": { ...Emprestimo } }`

---

### `PATCH /emprestimos/{id}/entregar` 🔒
Confirma a entrega física do livro (admin).

**Resposta 200:** `{ "message": "string", "emprestimo": { ...Emprestimo } }`

---

### `PATCH /emprestimos/{id}/cancelar` 🔒
Cancela um empréstimo pendente.

**Resposta 200:** `{ "message": "string", "emprestimo": { ...Emprestimo } }`

---

## Multas

### `GET /multas` 🔒
Lista todas as multas (admin).

**Resposta 200:** `{ "multas": [ ...Multa ] }`

---

### `GET /multas/minhas` 🔒
Lista as multas do aluno autenticado.

**Resposta 200:** `{ "multas": [ ...Multa ] }`

---

### `PATCH /multas/{id}/pagar` 🔒
Registra o pagamento de uma multa pelo aluno.

**Resposta 200:** `{ "message": "string", "multa": { ...Multa } }`

---

### `PATCH /multas/{id}/quitar` 🔒
Quita uma multa manualmente (admin).

**Resposta 200:** `{ "message": "string", "multa": { ...Multa } }`

---

## Engajamento

### Comentários

#### `POST /books/{id}/comments` 🔒
Cria um comentário em um livro. A publicação é assíncrona (fila).

**Body:**
```json
{
  "conteudo": "string (1–2000 chars)",
  "parentId": "string (opcional — ID do comentário pai para respostas)"
}
```

**Resposta 202:**
```json
{
  "message": "string",
  "queued": true
}
```

**Resposta 400:** Conteúdo inválido.  
**Resposta 404:** Livro não encontrado.

---

#### `GET /books/{id}/comments` 🔒
Lista os comentários de um livro, com respostas aninhadas e contagem de likes.

**Resposta 200:** Array de `Comentario`:
```json
[
  {
    "id": "string",
    "usuarioId": "string",
    "livroId": "string",
    "parentId": "string | null",
    "conteudo": "string",
    "deletado": false,
    "criadoEm": "datetime",
    "atualizadoEm": "datetime",
    "usuario": { "id": "string", "nome": "string", "email": "string" },
    "parent": { ...Comentario simplificado } ,
    "respostas": [ ...Comentario simplificado ],
    "likesCount": 12
  }
]
```

---

### Likes

#### `POST /comments/{id}/likes` 🔒
Curte ou descurte um comentário (toggle). A ação é assíncrona (fila).

**Resposta 202:**
```json
{
  "message": "string",
  "queued": true
}
```

**Resposta 404:** Comentário não encontrado.

---

### Avaliações (Ratings)

#### `POST /books/{id}/ratings` 🔒
Cria ou atualiza a avaliação do usuário autenticado para um livro.

**Body:**
```json
{
  "nota": 5,
  "texto": "string (opcional, max 1000 chars)"
}
```

**Resposta 200:**
```json
{
  "id": "string",
  "nota": 5,
  "texto": "string | null",
  "criadoEm": "string",
  "livro": { "id": "string", "titulo": "string" }
}
```

**Resposta 400:** Nota fora do range (1–5).  
**Resposta 404:** Livro não encontrado.

---

#### `DELETE /books/{id}/ratings` 🔒
Remove a avaliação do usuário autenticado para um livro.

**Resposta 200:** `{ "message": "string" }`  
**Resposta 404:** Avaliação não encontrada.

---

## Modelos de Dados

### User
```json
{
  "id": "string",
  "matricula": "string",
  "nome": "string",
  "email": "string",
  "role": "ALUNO | ADMIN",
  "criadoEm": "string"
}
```

### Book
```json
{
  "id": "string",
  "titulo": "string",
  "autor": "string",
  "isbn": "string",
  "sinopse": "string | null",
  "capaUrl": "string | null",
  "categoria": "string",
  "anoPublicacao": 2024,
  "editora": "string | null",
  "idioma": "string | null",
  "paginas": 432,
  "ativo": true,
  "criadoEm": "string",
  "totalExemplares": 5,
  "exemplaresDisponiveis": 3,
  "mediaAvaliacao": 4.5,
  "exemplares": [ ...Exemplar ]
}
```

### Exemplar
```json
{
  "id": "string",
  "numeroTombo": "string",
  "status": "DISPONIVEL | EMPRESTADO | INDISPONIVEL"
}
```

### Emprestimo
```json
{
  "id": "string",
  "usuarioId": "string",
  "exemplarId": "string",
  "status": "string",
  "motivoNegacao": "string | null",
  "dataSolicitacao": "datetime",
  "dataAprovacao": "datetime | null",
  "dataDevolucaoPrevista": "datetime | null",
  "dataDevolucaoReal": "datetime | null"
}
```

### FilaEspera
```json
{
  "id": "string",
  "usuarioId": "string",
  "livroId": "string",
  "posicao": 3,
  "status": "string"
}
```

### Multa
```json
{
  "id": "string",
  "emprestimoId": "string",
  "usuarioId": "string",
  "valorTotal": 15.00,
  "diasAtraso": 5,
  "status": "string",
  "dataPagamento": "datetime | null",
  "criadoEm": "datetime"
}
```

### Comentario
```json
{
  "id": "string",
  "usuarioId": "string",
  "livroId": "string",
  "parentId": "string | null",
  "conteudo": "string",
  "deletado": false,
  "criadoEm": "datetime",
  "atualizadoEm": "datetime",
  "usuario": { "id": "string", "nome": "string", "email": "string" },
  "respostas": [],
  "likesCount": 0
}
```

### Rating
```json
{
  "id": "string",
  "nota": 5,
  "texto": "string | null",
  "criadoEm": "string",
  "livro": { "id": "string", "titulo": "string" }
}
```

---

> 🔒 = Requer autenticação via Bearer Token  
> Ações assíncronas (comentários, likes) retornam `202 Accepted` com `"queued": true`
