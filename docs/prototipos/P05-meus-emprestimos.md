# P-05 — Meus Empréstimos

**Tela alvo:** Tela Empréstimos Aluno (Issue #5)
**Cobre os requisitos:** RF05, RF05.1, RF05.3, RF05.4, RF05.5, RF06, RF06.1

---

## Layout (top → bottom)

### 1. TopBar
- Esquerda: ícone de menu (hamburger).
- Centro: texto **"UNIFOR"** bold `UniforPrimary`.
- Direita: avatar circular do usuário.

### 2. Header
- **Título grande:** **"Meus Empréstimos"** — fonte black ~32sp, `UniforPrimary`.
- **Subtítulo:** "Gerencie seus livros ativos e acompanhe prazos de devolução." — cinza/`UniforOutline`.

### 3. Card "Status Geral" — RF05.1
- Fundo cinza-claro (`UniforSurfaceContainerHigh`), arredondado.
- Label superior pequeno uppercase: **"STATUS GERAL"**.
- Frase principal: **"Você possui 3 livros em curso"** bold `UniforPrimary`.
- Linha de duas métricas:
  - **`02`** grande verde + label "NO PRAZO" cinza pequeno.
  - **`01`** grande vermelho + label "ATRASADO" cinza pequeno.
- *(Há um ícone "fantasma" decorativo no fundo do card — silhueta cinza muito clara.)*

### 4. Banner azul "Dúvidas sobre multas?" — RF06, RF06.1
- Card retangular fundo `UniforPrimary` (azul-marinho).
- Ícone de info **(i)** branco no topo.
- Título: **"Dúvidas sobre multas?"** branco bold.
- **Botão "Saiba mais sobre multas"** dentro do card — fundo branco translúcido, texto branco.
  - Ao clicar → navega para P-06 (Guia de Política de Multas).

### 5. Lista de Empréstimos
Cada item é um **card** com **borda lateral colorida** indicando status.

#### 5.1 Item ATRASADO
- **Borda esquerda vermelha** sólida ~4dp de largura (cor [NOVA-COR1] vermelho de erro).
- Tag superior uppercase: ⚠️ **"ATRASADO"** vermelho com ícone de alerta circular.
- **Capa** miniatura do livro à esquerda.
- À direita:
  - Título bold: "Fundamentos da Termodinâmica".
  - Autores: "Claus Borgnakke, Richard E. Sonntag" cinza.
  - Linha de duas datas (label uppercase + valor):
    - **RETIRADA:** "12 Mai, 2024" cinza.
    - **PRAZO LIMITE:** "26 Mai, 2024" **vermelho** (RF05.5 cumprido em estado vencido).
  - **Box rosa-claro:** label uppercase pequeno cinza "MULTA ACUMULADA" + valor grande **"R$ 5,50"** vermelho + texto pequeno "Atualizado hoje".

#### 5.2 Item EM DIA (1)
- **Sem borda lateral colorida** (ou borda verde leve).
- Tag superior: ✅ **"EM DIA"** verde com ícone de check circular.
- Capa + título "Inteligência Artificial: Uma Abordagem Moderna".
- Autores: "Stuart Russell, Peter Norvig".
- Datas: RETIRADA "18 Mai, 2024" / PRAZO LIMITE "01 Jun, 2024" — ambas cinza/normais.
- **Botão "RENOVAR"** abaixo, full-width, fundo cinza-claro, texto cinza-escuro uppercase bold.

#### 5.3 Item EM DIA (2)
- Mesma estrutura do 5.2.
- Título: "Cálculo Diferencial e Integral" — autor "James Stewart".
- Datas: RETIRADA "20 Mai, 2024" / PRAZO LIMITE "03 Jun, 2024".
- Botão **"RENOVAR"**.

### 6. BottomNav
- Itens visíveis no mock: **INÍCIO · EMPRÉSTIMOS · ALERTAS · PERFIL**.
- Estado ativo: **EMPRÉSTIMOS** (chip azul-claro).

> ⚠️ Mesma observação de P-02/P-03: o mock usa "Alertas" no lugar de "Catálogo". Padronizar para a BottomNav oficial RF03.8 (Início, Catálogo, Empréstimos, Perfil) durante implementação.

---

## Estados que esta tela precisa cobrir

Conforme RF05 + RF05.3..RF05.7, a tela "Empréstimos" precisa exibir múltiplos status. **Este protótipo (P-05) cobre apenas:**
- ✅ ATRASADO (RF05.5 — prazo expirado, multa acumulada)
- ✅ EM DIA (livro emprestado, dentro do prazo)

**Faltam estados pendentes para protótipo (verificar com cliente):**
- ⏳ PENDENTE (aguardando análise — RF05.3)
- ✅ APROVADO (com data limite de retirada — RF05.5)
- ❌ RECUSADO (com motivo da recusa — RF05.7)
- ↩️ DEVOLVIDO (histórico)

Sugestão: adicionar abas/segmented no topo da lista para filtrar por status.

---

## Comportamento esperado

- Tap em **"Saiba mais sobre multas"** → navega para P-06 (Guia de Multas). RF06.1.
- Tap em um **card de empréstimo** → navega para a Página do Livro (P-03/P-04).
- Tap em **"RENOVAR"** *(itens EM DIA)* → tenta renovar o empréstimo (regra dependente do Guia de Multas).
- O card ATRASADO **não tem** botão renovar — implícito que livro em atraso não pode renovar (ver Guia de Multas).

## Componentes reutilizáveis identificados

- `OverallStatusCard` — card "Status Geral" com métrica dupla (NO PRAZO / ATRASADO).
- `InfoBanner` — banner azul com CTA (variante azul do `WarningBlock` de P-02).
- `LoanItemCard` — card de empréstimo com borda lateral status-colorida (suporta variantes ATRASADO / EM DIA / PENDENTE / APROVADO / RECUSADO / DEVOLVIDO).
- `LoanStatusTag` — tag uppercase com ícone (verde EM DIA / vermelho ATRASADO / amarelo PENDENTE / azul APROVADO etc).
- `FineBox` — caixa rosa-clara com valor de multa.
- `RenewButton` — botão secundário cinza largura cheia.
- `DateLabelPair` — bloco "LABEL UPPERCASE + valor" (também útil em P-04 ficha técnica).

## Cores novas a adicionar em `Color.kt`

| Token sugerido | Hex aproximado | Uso |
|----------------|----------------|-----|
| `UniforError` | `#D43A3A` (a confirmar com mock pixel-picker) | borda lateral / texto ATRASADO |
| `UniforErrorContainer` | `#FCE8E8` | fundo da `FineBox` |
| `UniforOnErrorContainer` | `#A11A1A` | texto vermelho dentro da `FineBox` |
| `UniforSuccess` | `#1E8E3E` | tag "EM DIA" / "NO PRAZO" |
