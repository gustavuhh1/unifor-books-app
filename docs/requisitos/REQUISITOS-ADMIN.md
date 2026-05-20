# Requisitos Funcionais — Perfil ADMINISTRADOR

> Transcrição literal da planilha de requisitos. Códigos preservados como na fonte.
> ⚠️ A planilha original possui **códigos duplicados** intencionalmente (dois `RF02` para o Admin: um para moderação/banimento, outro para visualização de denúncias). Mantemos a duplicação e adicionamos um sufixo entre parênteses para desambiguar internamente, **sem alterar o código oficial**.

---

## RF01 — Autenticação do Administrador

| Código | Funcionalidade |
|--------|----------------|
| **RF01.0** | O sistema deve permitir que o Administrador possa se autenticar de forma exclusiva para ter autorizações para utilizar dos métodos de administrador |
| **RF01.2** | A tela de autenticação do administrador deve possuir os campos email, senha, esqueceu a senha, botão de confirmação |
| **RF01.3** | Ao se autenticar com sucesso deve ser redirecionado para a página home do administrador |
| **RF01.4** | A tela home do administrador deve poder ter a mesma visualização de interface do usuário/aluno, com adição de suas funcionalidades como administrador. |

> **Nota:** A planilha pula de RF01.0 → RF01.2 (não há RF01.1 documentado).

---

## RF02 — Moderação *(primeiro RF02)*

| Código | Funcionalidade |
|--------|----------------|
| **RF02** *(moderação)* | O sistema deve permitir que o administrador possa deletar comentários, para moderação. |
| **RF02.1** *(moderação)* | O sistema deve permitir que o administrador realize banimentos temporários/permanentes de usuários/alunos. |

---

## RF02 — Denúncias *(segundo RF02)*

> ⚠️ **Códigos duplicados intencionalmente na planilha original.**

| Código | Funcionalidade |
|--------|----------------|
| **RF02** *(denúncias)* | O sistema deve disponibilizar uma tela para visualizar todas denuncias feitas pelos usuários/alunos. |
| **RF02.1** *(denúncias)* | O sistema deve disponibilizar uma opção de "Denunciar comentário" em cada comentário publicado na comunidade. |
| **RF02.3** | O sistema deve permitir que o aluno informe o motivo da denúncia ao registrar uma denúncia de comentário. |
| **RF02.4** | O sistema deve registrar a denúncia e encaminhá-la para análise do administrador. |
| **RF02.5** | O sistema deve notificar visualmente ao aluno que a denúncia foi enviada com sucesso para revisão. |
| **RF02.6** | O sistema deve disponibilizar ao administrador uma área para visualização dos comentários denunciados. |
| **RF02.7** | O sistema deve redirecionar o comentário denunciado ao clicá-lo |

> **Nota:** A planilha pula de RF02.1 → RF02.3 (não há RF02.2 documentado neste bloco).

---

## RF03 — Gerenciamento de Empréstimos

| Código | Funcionalidade |
|--------|----------------|
| **RF03** | O Sistema deve possuir uma tela para gerenciar todos empréstimos e devoluções |
| **RF03.2** | O Sistema deve permitir que o administrador aprove ou negue uma solicitação de empréstimo |
| **RF03.3** | O Sistema deve permitir que o administrador ao negar uma solicitação de empréstimo, ele insira o motivo da negação. |
| **RF03.4** | O Sistema deve permitir que o administrador ao aprovar uma solicitação de empréstimo, ele insira um prazo de recebimento. |
| **RF03.5** | O Sistema deve permitir que o administrador confirme que ao recebimento ser concluído altere o status do empréstimo para "em andamento" |
| **RF03.6** | O Sistema deve permitir que o administrador confirme ao recebimento do livro, altere o status do empréstimo para "concluido" |

> **Nota:** A planilha pula de RF03 → RF03.2 (não há RF03.1 documentado).

---

## RF04 — Eventos / Notícias

| Código | Funcionalidade |
|--------|----------------|
| **RF04** | O Sistema deve possuir uma tela para gerenciamento de eventos e notícias. |
| **RF04.1** | O sistema deve permitir que o administrador editar/criar/deletar eventos/notícias. |
| **RF04.2** | O sistema deve permitir que o administrador altere quais eventos irão aparecer na tela principal do aluno/usuário. |
| **RNF04.3** | O sistema deve permitir no máximo dois eventos/notícias que serão exibidos na tela principal (home) . |

> **Nota:** O item **RNF04.3** é classificado como **Requisito Não-Funcional** (regra de limite quantitativo) pelo prefixo `RNF`. Mantido junto ao bloco RF04 por afinidade temática (como na planilha original).

---

## RF05 — Disponibilidade de Livros

| Código | Funcionalidade |
|--------|----------------|
| **RF5.0** | O Sistema deve permitir que o administrador ao acessar um livro, ele possa alterar o status de disponibilidade para emprestamento,"disponível", "indisponível". |
| **RF5.1** | O Sistema deve permitir que o administrador bloqueie o empréstimo para todos usuários, caso o livro esteja indisponível. |

> **Nota:** Códigos `RF5.0` e `RF5.1` *(sem zero à esquerda)* preservados como na fonte.

---

## RF06 — Confirmação de Devolução

| Código | Funcionalidade |
|--------|----------------|
| **RF6.0** | O Sistema deve permitir que o administrador possa confirmar a devolução de exemplar. |
| **RF6.1** | O Sistema deve atualizar o horário de devolução no momento em que o Administrador confirme a entrega. |

---

## Mapa de Telas → Requisitos (Admin)

| Tela / Issue | Requisitos |
|--------------|------------|
| Login Admin | RF01.0, RF01.2, RF01.3, RF01.4 |
| Home Admin (espelhada do aluno) | RF01.4 |
| Painel de Moderação (#11) | RF02 *(moderação)*, RF02.1 *(moderação)* |
| Tela de Banimento (#14) | RF02.1 *(moderação)* |
| Tela de Denúncias / Resumo de Denúncias (#13) | RF02 *(denúncias)*, RF02.1, RF02.3, RF02.4, RF02.5, RF02.6, RF02.7 |
| Empréstimos — Solicitações (#15) | RF03, RF03.2, RF03.3, RF03.4 |
| Empréstimos — Aguardando Retirada (#16) | RF03.4, RF03.5 |
| Empréstimos — Em Atraso (#17) | RF03.5, RF03.6 |
| Confirmação de Devolução | RF6.0, RF6.1 |
| Tela de Livro Adm (#7) | RF5.0, RF5.1 |
| Monitorar Acervo (#9) | RF5.0, RF5.1 |
| Gerenciamento de Eventos | RF04, RF04.1, RF04.2, RNF04.3 |
