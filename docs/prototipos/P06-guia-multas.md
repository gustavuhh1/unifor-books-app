# P-06 — Guia de Política de Multas

**Tela alvo:** Guia de Políticas (acessada de "Meus Empréstimos")
**Cobre os requisitos:** RF06, RF06.1

---

## Layout (top → bottom)

### 1. TopBar
- Esquerda: ícone de **voltar** (chevron `<`).
- Centro: texto **"UNIFOR"** bold `UniforPrimary`.
- Direita: avatar circular do usuário.

### 2. Header / Introdução
- **Título grande:** **"Guia de Política de Multas"** — black ~28sp, `UniforPrimary`.
- **Parágrafo introdutório:** "Garantir o acesso equitativo à nossa coleção começa com devoluções pontuais. Entenda como funciona nosso sistema de empréstimos e mecanismos de multa." — regular cinza/`UniforOutline`.

### 3. Bloco "Regras para Devoluções"
- Cabeçalho de seção:
  - Ícone azul de calendário em quadrado azul-claro arredondado.
  - Texto bold `UniforPrimary`: **"Regras para Devoluções"**.
- Card branco com borda leve:
  - Subtítulo: **"Período de 2 semanas"** bold `UniforPrimary`.
  - Texto: "Duração padrão de empréstimo para todos os itens de circulação geral. Renovações disponíveis se não houver reservas." cinza.

### 4. Bloco "Penalidades por Atraso"
- Card fundo rosa-claro/`UniforErrorContainer` com borda leve:
  - Ícone de alerta vermelho à esquerda em quadrado rosa-mais-escuro.
  - Subtítulo: **"Penalidades por Atraso"** bold cor vermelho-escuro/`UniforOnErrorContainer`.
  - Texto: "As multas começam a acumular diariamente após o prazo. Sua conta será temporariamente suspensa para novos empréstimos até a regularização." cinza-escuro.

### 5. Cronograma de Acúmulo de Multas
- Cabeçalho: **"Cronograma de Acúmulo de Multas"** bold `UniforPrimary`.
- Lista vertical de **4 cards**, cada um representando uma faixa de atraso, identificados por **ícone colorido à esquerda em quadrado pequeno**:

| # | Cor do ícone | Significado provável (a confirmar) |
|---|--------------|-----------------------------------|
| 1 | Verde (✅)   | Em dia / dentro do prazo |
| 2 | Amarelo (⚠️) | 1–7 dias de atraso (multa leve) |
| 3 | Rosa-pêssego | 8–14 dias (multa intermediária) |
| 4 | Vermelho (❌) | 15+ dias / suspensão |

> ⚠️ **No mock os textos de cada faixa estão como placeholder duplicado** (todos repetem "GARANTIR O ACESSO EQUITATIVO À NOSSA COLEÇÃO COMEÇA COM DEVOLUÇÕES PONTUAIS. ENTENDA COMO FUNCIONA NOSSO SISTEMA DE EMPRÉSTIMOS E MECANISMOS DE MULTA." em uppercase + lorem ipsum repetido). Os **textos reais de cada faixa** **não foram entregues** nesta versão do protótipo. **Pendência:** solicitar ao cliente o conteúdo definitivo.

Cada card contém:
- Quadrado pequeno colorido com ícone à esquerda.
- Bloco de texto à direita:
  - Subtítulo uppercase pequeno (placeholder no mock).
  - Parágrafo descritivo (placeholder no mock).

### 6. Perguntas Frequentes (Accordion / FAQ)
Cabeçalho: **"Perguntas Frequentes"** bold `UniforPrimary`.

Lista de perguntas em formato accordion (cada item tem chevron à direita indicando expansão).

#### 6.1 Pergunta expandida (mostrada no mock)
- **Q:** "Como faço para pagar minhas multas?"
- **A:** "Os pagamentos podem ser feitos através do aplicativo móvel Unifor, Portal do Aluno (NetU) ou no balcão de circulação da Biblioteca usando cartões de crédito/débito."

#### 6.2 Pergunta colapsada
- **Q:** "Posso renovar um item que já está em atraso?"
- **A:** "Não. Quando um item está em atraso, as renovações são bloqueadas. Você deve devolvê-lo e quitar a multa antes de emprestar ou renovar novamente."

#### 6.3 Pergunta colapsada
- **Q:** "O que acontece se eu perder um livro?"
- **A:** "Em caso de perda, o aluno é responsável pelo custo de reposição da edição mais recente disponível, acrescido de uma taxa administrativa de processamento."

### 7. BottomNav
- Itens visíveis: INÍCIO · EMPRÉSTIMOS · ALERTAS · PERFIL.
- Estado ativo: **EMPRÉSTIMOS**.

> ⚠️ Mesma observação: padronizar para BottomNav oficial RF03.8 (Início, Catálogo, Empréstimos, Perfil).

---

## Comportamento esperado

- Tap em **chevron** de cada FAQ → expande/colapsa o item.
- Botão de voltar → retorna a `Empréstimos` (P-05).

## Componentes reutilizáveis identificados

- `SectionHeader` — ícone-em-quadrado + título bold (reutilizável em outras seções com cabeçalho temático).
- `RuleCard` — card branco com subtítulo + texto.
- `WarningCard` — variante do `RuleCard` com fundo rosa/vermelho-claro (compartilha estilo com o `WarningBlock` de P-02).
- `ScheduleStepRow` — linha do cronograma de multas (ícone colorido + bloco texto).
- `FaqAccordion` — lista de perguntas expansíveis.
  - Subcomponente `FaqItem` (estado expandido/colapsado).

---

## Pendências

1. **Textos definitivos do "Cronograma de Acúmulo de Multas"** — atualmente placeholder no mock.
2. **Mais perguntas no FAQ?** — só 3 estão documentadas; pode haver mais.
