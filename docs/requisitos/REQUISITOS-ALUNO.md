ude# Requisitos Funcionais — Perfil ALUNO

> Transcrição literal da planilha de requisitos. Códigos preservados como na fonte.
> ⚠️ A planilha original possui **códigos duplicados** intencionalmente (dois `RF04`, dois `RF02` etc.). Mantemos a duplicação e adicionamos um sufixo entre parênteses para desambiguar internamente, **sem alterar o código oficial**.

---

## RF01 — Tela de Login

| Código | Funcionalidade |
|--------|----------------|
| **RF01** | O sistema deve ter uma tela de login com um campo de matrícula, um campo de senha e botões para "Entrar", "Esqueceu a senha?" e "Não tem uma conta? Cadastre-se" |
| **RF01.1** | O sistema deve redirecionar para a tela de cadastro ao clicar no botão "Não tem uma conta? Cadastre-se" |
| **RF01.2** | O sistema deve permitir que o usuário realize login informando matrícula e senha cadastradas. |
| **RF01.3** | O sistema deve validar os dados informados no login antes de permitir o acesso ao aplicativo. |
| **RF01.4** | O sistema deve exibir uma mensagem de erro caso a matrícula ou a senha informada esteja incorreta. |
| **RF01.5** | O sistema deve redirecionar o usuário para a tela inicial do aplicativo após login realizado com sucesso. |
| **RF01.6** | O sistema deve redirecionar o usuário para o fluxo de recuperação de senha ao clicar em "Esqueceu a senha?". |
| **RF01.7** | O sistema deve permitir que o usuário visualize ou oculte a senha digitada no campo de senha, caso essa funcionalidade seja adotada na interface. |

---

## RF02 — Recuperação de Senha

| Código | Funcionalidade |
|--------|----------------|
| **RF02.0** | Tela de recuperar senha deve conter campo de email ou matrícula, e botão "Enviar" para receber o token de recuperação de senha |
| **RF02.1** | O sistema deve aceitar um token válido enviado via-email, e liberar um campo "Nova-senha" para redefinir a senha-atual. |
| **RF02.2** | O sistema deve valida esse token para evitar falhas de segurança |

---

## RF03 — Tela Home

| Código | Funcionalidade |
|--------|----------------|
| **RF03.0** | O sistema deve ter uma tela Home com campo de busca por livro, seção de categorias, seção de livros mais bem avaliados, mural de eventos da biblioteca, seção "Descubra mais", seção "Novidades" e uma barra de navegação inferior. |
| **RF03.1** | O sistema deve permitir que o usuário pesquise livros por meio de um campo de busca disponível na tela Home. |
| **RF03.2** | O sistema deve exibir categorias de livros para facilitar a navegação do usuário pelo acervo. |
| **RF03.3** | O sistema deve permitir que o usuário acesse a listagem de livros de uma categoria ao selecionar uma categoria na tela Home. |
| **RF03.4** | O sistema deve exibir uma seção com os livros mais bem avaliados pelos usuários. E poder acessar o livro na sessão |
| **RF03.5** | O sistema deve exibir um mural de eventos com informações publicadas pela biblioteca |
| **RF03.6** | O sistema deve exibir a seção "Descubra mais" com sugestões variadas de livros para o usuário. |
| **RF03.7** | O sistema deve exibir a seção "Novidades" com os livros adicionados recentemente ao acervo. |
| **RF03.8** | O sistema deve ter uma barra de navegação inferior com as opções "Início", "Catálogo", "Empréstimos" e "Perfil". |

---

## RF04 — Tela de Catálogo *(primeiro RF04)*

| Código | Funcionalidade |
|--------|----------------|
| **RF04** *(catálogo)* | O sistema deve possuir uma tela "Catálogo", ao clicar no botão Catálogo, usuario deve ser redirecionado ao catálogo. |
| **RF04.1** *(catálogo)* | O sistema deve possuir uma grande exibição de livros no catálogo, para busca de exemplares/livros |
| **RF04.2** *(catálogo)* | O sistema deve possuir uma barra de pesquisa para buscar livros tanto por, Titulo e Autor. |
| **RF04.3** *(catálogo)* | O sistema no catálogo deve possuir seletor de categorias para buscar livros por categorias. |
| **RF04.4** *(catálogo)* | O sistema deve exibir todos o resultado compondo, Imagem, Titulo, autor e avaliações (Estrelas) do livro. |
| **RF04.5** *(catálogo)* | O sistema deve redirecionar o usuário ao livro clicado no catálogo. |

---

## RF04 — Tela de Visualização do Livro *(segundo RF04)*

> ⚠️ **Códigos duplicados intencionalmente na planilha original.** Esta seção descreve a *Página do Livro*, cujos itens (RF04.1, RF04.2, RF04.3, RF04.4, RF04.5) reaproveitam os mesmos números do bloco anterior com **funcionalidades diferentes**. Preservamos como entregue.

| Código | Funcionalidade |
|--------|----------------|
| **RF04** *(livro)* | O sistema deve ter uma tela de visualização de livro com imagens da obra, título, autor, descrição, data de publicação, status do livro (disponível/indisponível), botão de solicitar empréstimo, média/resumo de avaliações e comentários da comunidade. |
| **RF04.05** | O sistema deve exibir resumo das métricas de avaliações do livro exibido, Média de estrelas, Total de avaliações e Quantas páginas o livro possui |
| **RF04.1** *(livro)* | O sistema deve exibir um botão de "Solicitar empréstimo" na tela de visualização do livro. |
| **RF04.2** *(livro)* | O sistema deve permitir que o usuário visualize a lista de comentários associados ao livro selecionado. |
| **RF04.2.1** | O Comentário deve conter: Image perfil, Nome, há quanto tempo, comentário (texto), Botão denunciar (bandeira), botão curtir, botão responder |
| **RF04.3** *(livro)* | O sistema deve permitir que usuario avalie o livro, com estrelas de (0.5, 1.0, 1.5 ... 5) |
| **RF04.4** *(livro)* | O sistema deve permitir que o usuário envie comentários ao livro exibido. |
| **RF04.5** *(livro)* | O sistema deve permitir que o usuário curta ou comente, comentários de outros usuários. |
| **RF04.6** | O sistema deve permitir que o usuário ordene a visualização de comentários em, "relevância", "mais recentes", "mais curtidas". |
| **RF04.7** | O sistema deve permitir que usuário envie uma denúncia referente a um comentário, ao clicar na bandeira no comentário |
| **RF04.8** | Quando o usuário clicar na bandeira, deve ser redirecionado a tela de "Denunciar Comentário" |
| **RF04.9** | O Sistema deve solicitar o dados, Motivo denúncia (Select) ex: Conteúdo Ofensivo e Assédio, Spam e etc, e texto opcional para explicitar a denúncia, e botão de "Enviar denuncia" |
| **RF04.10** | A interface deve corresponder de acordo com a resposta do servidor, caso a denuncia seja envia com sucesso, deve possuir texto de confirmação e dois botões "Voltar para o livro" e "Ir para o início"(tela Home). |

---

## RF05 — Tela de Empréstimos

| Código | Funcionalidade |
|--------|----------------|
| **RF05** | O sistema deve ter uma tela de "Empréstimos" para exibir ao usuário os livros emprestado e atrasados,os livros solicitados, aprovados, recusados, emprestados, devolvidos e atrasados. |
| **RF05.1** | A interface deve possuir uma caixa com resumo dos empréstimos com numero totais de empréstimos ativos, e quantos estão no intervalo correto e quantos estão atrasados. |
| **RF05.1.1** | O sistema deve registrar uma solicitação de empréstimo quando o usuário clicar no botão "Solicitar empréstimo" na tela do livro. |
| **RF05.2** | O sistema deve redirecionar o usuário para a tela de "Empréstimos" após a solicitação do livro. |
| **RF05.3** | O sistema deve exibir o livro solicitado com o status "Pendente" enquanto a solicitação estiver aguardando análise do administrador. |
| **RF05.4** | O sistema deve atualizar o status da solicitação para "Aprovado" quando o administrador autorizar o empréstimo do livro. |
| **RF05.5** | O sistema deve exibir a data e a hora limite para retirada do livro na biblioteca quando a solicitação for aprovada. |
| **RF05.6** | O sistema deve atualizar o status da solicitação para "Recusado" quando o administrador negar o empréstimo. |
| **RF05.7** | O sistema deve exibir ao usuário informando o motivo da recusa quando a solicitação de empréstimo for negada pelo administrador. |
| **RF05.8** | O sistema deve exibir uma mensagem informando que o livro está indisponível quando a solicitação for recusada por indisponibilidade do exemplar. |
| **RF05.8.1** | O Sistema deve exibir um botão "Entra na fila" Caso o livro já esteja em "empréstimo". |

---

## RF06 — Guia de Políticas

| Código | Funcionalidade |
|--------|----------------|
| **RF06** | O sistema deve possuir uma seção informativa denominada "Guia de Políticas" na área de Empréstimos, contendo orientações sobre o funcionamento dos empréstimos, devoluções, prazos de retirada e aplicação de multas por atraso. |
| **RF06.1** | O sistema deve permitir que o usuário acesse o "Guia de Políticas" pela tela de Empréstimos. |

---

## RF07 — Denúncia de Comentários

| Código | Funcionalidade |
|--------|----------------|
| **RF07** | O sistema deve permitir que o aluno denuncie comentários que violem as políticas da comunidade. |
| **RF07.1** | O sistema deve disponibilizar uma opção de "Denunciar comentário" em cada comentário publicado na comunidade. |
| **RF07.2** | O sistema deve permitir que o aluno informe o motivo da denúncia ao registrar uma denúncia de comentário. |
| **RF07.3** | O sistema deve registrar a denúncia e encaminhá-la para análise do administrador. |
| **RF07.4** | O sistema deve notificar visualmente ao aluno que a denúncia foi enviada com sucesso para revisão. |
| **RF07.5** | O sistema deve disponibilizar ao administrador uma área para visualização dos comentários denunciados. |

---

## RF8 — Tela de Perfil

| Código | Funcionalidade |
|--------|----------------|
| **RF8.0** | O sistema deve possuir uma tela "Perfil", será exibido, nome, email e foto de perfil do usuário aluno. |
| **RF8.1** | O sistema deve permitir que o usuário/aluno possa alterar sua foto de perfil. |
| **RF8.2** | O sistema deve permitir denúncias para fotos de perfis que violem as políticas institucionais da organização |

---

## Mapa de Telas → Requisitos (Aluno)

| Tela | Requisitos |
|------|------------|
| Login | RF01, RF01.1..RF01.7 |
| Recuperar Senha | RF02.0, RF02.1, RF02.2 |
| Cadastro (referenciado por RF01.1) | *— sem detalhamento próprio na planilha* |
| Home | RF03.0..RF03.8 |
| Catálogo | RF04 *(catálogo)* + RF04.1..RF04.5 *(catálogo)* |
| Página do Livro | RF04 *(livro)*, RF04.05, RF04.1..RF04.5 *(livro)* |
| Comentários (componente) | RF04.2.1, RF04.4, RF04.5, RF04.6 |
| Avaliação | RF04.3 *(livro)* |
| Denunciar Comentário | RF04.7, RF04.8, RF04.9, RF04.10 |
| Empréstimos | RF05, RF05.1..RF05.8.1 |
| Guia de Políticas | RF06, RF06.1 |
| Denúncia (geral) | RF07, RF07.1..RF07.5 |
| Perfil | RF8.0, RF8.1, RF8.2 |
