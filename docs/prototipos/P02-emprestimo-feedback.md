# P-02 — Feedback de Empréstimo (Aprovado / Indisponível)

**Tela alvo:** *feedback exibido após o usuário pressionar "Solicitar Empréstimo"*
**Cobre os requisitos:** RF05.1.1, RF05.2, RF05.5, RF05.7, RF05.8, RF05.8.1

A imagem mostra **dois cenários** apresentados em sequência (rolagem vertical da mesma página de protótipo). Ambos cabem em rotas/estados distintos da mesma tela na implementação real.

---

## TopBar (compartilhada)

- Esquerda: ícone de menu (hamburger).
- Centro/esquerda: texto **"UNIFOR"** em fonte bold cor `UniforPrimary`.
- Direita: avatar circular do usuário.

---

## Cenário 1 — Empréstimo Aprovado

Cabeçalho da seção (no protótipo): texto **"01.  Cenário de Sucesso"** em verde.

### Card principal (fundo branco, cantos grandes ~28dp, sombra leve)

1. **Ícone de check** — quadrado arredondado verde claro com check verde-escuro centralizado (ícone topo do card, centralizado).
2. **Título grande:** **"Empréstimo Aprovado!"** — fonte black, cor `UniforPrimary`, centralizado.
3. **Subtítulo:** "Sua solicitação foi processada com sucesso pelo sistema da Biblioteca Central." — fonte regular, cor cinza, centralizado.
4. **Bloco de "Data Limite para Retirada"** (sub-card cinza-claro arredondado):
   - Ícone de calendário verde à esquerda.
   - Label pequeno em maiúsculas: "DATA LIMITE PARA RETIRADA".
   - Valor grande: **"24 de Outubro, 2023"** em fonte bold `UniforPrimary`.
5. **Aviso destacado** (sub-card rosa-claro/`#FCE8E8` aprox.):
   - Ícone de alerta triangular vermelho à esquerda.
   - Texto: "Atenção: Após esta data, a reserva será cancelada automaticamente e o exemplar retornará ao acervo." — fonte regular vermelha-escura.
6. **Linha do livro reservado:**
   - Miniatura da capa à esquerda (60×80dp aprox.).
   - À direita: **título** ("Design for the Real World") em bold + **autor • ano** ("Victor Papanek • 1971") cinza.
7. **Botão CTA primary:** **"Ver Meus Empréstimos →"** — fundo `UniforPrimary`, texto branco, largura cheia, cantos arredondados ~12–16dp.

---

## Cenário 2 — Livro Indisponível

Cabeçalho da seção (no protótipo): texto **"02.  Cenário de Indisponibilidade"** em vermelho.

### Card principal (fundo branco, cantos grandes ~28dp)

1. **Ícone "proibido"** (círculo vermelho-claro com símbolo `🚫`) no canto superior esquerdo.
2. **Tag superior direita:** pílula cinza-clara com texto pequeno **"CÓDIGO: ERR_403"**.
3. **Título grande:** **"Livro Indisponível"** — fonte black, cor `UniforPrimary`, alinhado à esquerda.
4. **Subtítulo:** "Infelizmente, o exemplar selecionado não pode ser emprestado no momento." — fonte regular cinza.
5. **Faixa de status** (informação contextual):
   - Borda esquerda cinza-clara/divisória.
   - Ícone de info (i) cinza.
   - Label bold: "Status Atual: Em uso".
   - Texto explicativo: "O livro está atualmente em posse de outro aluno ou em processo de restauro técnico."
6. **Bloco "O que você pode fazer?"** (sub-card branco com borda):
   - Pergunta como cabeçalho.
   - Dois botões empilhados (cada um com ícone à esquerda):
     - 📌 **"Entrar na fila de reserva"** *(referente a RF05.8.1 — botão "Entra na fila")*
     - 📑 **"Buscar edições similares"**
7. **Rodapé do card** com 2 botões lado a lado:
   - **"Voltar"** — secundário, fundo cinza-claro, texto cinza-escuro.
   - **"Ver Acervo Digital"** — primário, fundo `UniforPrimary`, texto branco.

---

## BottomNav

- Variantes vistas no mock:
  - **Cenário 1:** Início · Empréstimos · Alertas · Perfil  *(item "ALERTAS" no lugar de Catálogo)*
  - **Cenário 2:** Início · Empréstimos · Alertas · Perfil
- Item ativo no protótipo: **EMPRÉSTIMOS** (com indicador azul-claro).

> ⚠️ **Inconsistência detectada:** este protótipo mostra a aba "Alertas" no lugar de "Catálogo" (que é o oficial em RF03.8). **Decisão:** manter a BottomNav oficial conforme RF03.8 (Início, Catálogo, Empréstimos, Perfil) e tratar "Alertas" como uma variante exploratória **não adotada**. Confirmar com o usuário antes da implementação se essa intenção mudou.

---

## Comportamento esperado

- **Cenário 1** é renderizado quando a solicitação é aprovada pelo administrador (estado `APROVADO` do empréstimo) **e/ou** quando o aluno acabou de fazer a solicitação com sucesso.
  - Mostra **data limite de retirada** → atende RF05.5.
- **Cenário 2** é renderizado quando o sistema responde "indisponível" (livro emprestado/bloqueado).
  - Botão "Entrar na fila de reserva" → atende RF05.8.1.
  - Mensagem de indisponibilidade → atende RF05.8.

## Componentes reutilizáveis identificados

- `FeedbackCard` — card grande com ícone topo + título + subtítulo + slots para conteúdo extra.
- `InfoBadge` — pílula com label de código (ex.: `CÓDIGO: ERR_403`).
- `WarningBlock` — bloco rosa-claro com ícone de alerta (variante do AlertBanner).
- `BookMiniRow` — linha compacta com capa miniatura + título + autor•ano (será usada também em P-05).
- `IconActionRow` — botão com ícone à esquerda + label.
- `PrimaryButton` / `SecondaryButton` (já temos uma versão em `LoginScreen` — promover para `ui/components/`).
