# P-01 — Catálogo de Livros

**Tela alvo:** Catálogo (acessada pela aba "CATÁLOGO" na BottomNav)
**Cobre os requisitos:** RF04 *(catálogo)*, RF04.1, RF04.2, RF04.3, RF04.4, RF04.5

---

## Layout (top → bottom)

### 1. TopAppBar
- **Esquerda:** ícone de menu (3 linhas, hamburger), cor azul `UniforPrimary`.
- **Centro/esquerda do título:** texto **"Unifor Books"** em fonte forte/bold, cor `UniforPrimary`.
- **Direita:** avatar circular (foto de perfil — feminina no mock, ilustração).
- Fundo branco/superfície neutra.

### 2. Campo de Busca
- Container retangular arredondado (cantos ~20–24dp), fundo cinza-claro (`#F1F2F4`/`UniforSurfaceContainerHigh`).
- **Ícone de lupa** à esquerda.
- **Placeholder:** "Pesquisar por título, autor ou ISBN".
- Sem botão lateral (diferente da Home, que tem o botão "Buscar").

### 3. Filtros (chips horizontais)
- Linha de chips **scroll horizontal**:
  1. **"Todos"** — chip selecionado, fundo `UniforPrimary` (azul-marinho), texto branco bold.
  2. **"Disponíveis"** — chip não-selecionado, fundo cinza-claro, texto preto.
  3. **"Computação"** — chip não-selecionado, fundo cinza-claro, texto preto.
- Indica que outras categorias seguem após "Computação" (corte horizontal sugere overflow).

### 4. Grid de Livros (2 colunas)
Cada **card** contém, de cima para baixo:

1. **Capa do livro** — imagem grande, ratio ~3:4, com **fundo neutro** (cinza-claro ou cinza-escuro dependendo da capa).
2. **Badge de status** logo abaixo da capa:
   - **DISPONÍVEL** → fundo `UniforSecondaryContainer` (`#8AFAA7`), texto `UniforSecondary` (`#006D35`), pílula arredondada, fonte maiúscula, peso black.
   - **EMPRESTADO** → fundo cinza-claro, texto cinza médio.
3. **Título do livro** — fonte bold ~16sp, cor `onSurface` (preto).
4. **Autor** — fonte regular ~13sp, cor cinza/`UniforOutline`.
5. **Avaliação** — linha com **5 estrelas** (`UniforTertiaryFixed` = `#FEBB2B`) + nota numérica ao lado (ex.: `4.8`, `4.0`, `5.0`, `4.7`).
   - Estrelas suportam meia-estrela visual (capturado em "4.8" e "4.7" no mock).

#### Exemplos de livros mostrados no protótipo
| Título | Autor | Status | Nota |
|--------|-------|--------|------|
| Introdução aos Algoritmos | Thomas H. Cormen | DISPONÍVEL | 4.8 |
| Design Sustentável | Johnathan Rose | EMPRESTADO | 4.0 |
| Direito Constitucional | Gilmar Mendes | DISPONÍVEL | 5.0 |
| Código Limpo | Robert C. Martin | DISPONÍVEL | 4.7 |

### 5. BottomNavBar
- 4 itens: **INÍCIO · CATÁLOGO · EMPRÉSTIMOS · PERFIL**.
- Item **CATÁLOGO** ativo: ícone azul + fundo de chip arredondado azul-claro, label `UniforPrimary`.
- Demais itens: ícone cinza, label cinza.

---

## Comportamento esperado

- Tap no card de livro → navega para Página do Livro (P-03 ou P-04). Ref: RF04.5 *(catálogo)*.
- Tap em chip de filtro → atualiza o grid filtrando por categoria/status. Ref: RF04.3 *(catálogo)*.
- Digitar na busca → filtra dinamicamente por título/autor (e ISBN, conforme placeholder). Ref: RF04.2 *(catálogo)*.

## Componentes reutilizáveis identificados

- `BookCardGrid` — versão grid do card (diferente do `BookCard` carrossel já existente em `HomeScreen.kt`).
- `StatusBadge` — badge `DISPONÍVEL` / `EMPRESTADO` (reutilizável em P-03, P-04, P-05).
- `RatingStars` — linha de 5 estrelas com suporte a meia-estrela + nota numérica.
- `FilterChipsRow` — linha de chips horizontais scrollável.
- `CatalogSearchBar` — campo de busca sem botão (variante do `SearchSection` da Home).
