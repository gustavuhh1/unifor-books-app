# P-04 — Detalhes do Livro (versão "Empréstar agora")

**Tela alvo:** variante alternativa da Página do Livro (Issue #6)
**Cobre os requisitos:** RF04 *(livro)*, RF04.05, RF04.1 *(livro)*, RF04.2 *(livro)*, RF04.2.1, RF04.4 *(livro)*, RF04.5 *(livro)*

> Esta versão aparece **três vezes** no material entregue (mesma imagem repetida). Ela representa um *layout alternativo* para a Página do Livro, mais enxuto que P-03. **Decisão de produto:** confirmar com o usuário qual layout adotar — sugestão é **P-03 como base** (cobre mais requisitos) e **P-04 como referência de seções específicas**: ficha técnica + título do header.

---

## Layout (top → bottom)

### 1. TopBar
- Esquerda: **chevron `<`** (voltar).
- Centro: título **"Detalhes do Livro"** (texto pequeno, cinza-escuro/`onSurface`).
- Direita: dois ícones — **share** (📤) e **bookmark** (🔖 ).

### 2. Capa
- Capa grande centralizada, fundo escuro contrastante, ratio ~3:4.
- **Badge "DISPONÍVEL"** sobreposto no canto superior direito *(diferente de P-03 que pôs no canto superior esquerdo)* — fundo `UniforSecondaryContainer`, texto `UniforSecondary`.

### 3. Título e Autor
- Título: **"A Perspectiva da Inovação Digital"** — black ~26sp, `UniforPrimary`.
- Autores: **"Dr. Ricardo Menezes & Profa. Elena Souza"** — regular cinza.

### 4. Tags / Categorias
Linha de chips outlined cinza-claro:
- "Gestão"
- "Tecnologia"
- "2024"

### 5. Métricas em linha (3 itens) — RF04.05
- **Avaliação:** `4.8` com **estrelas amarelas** + texto "(124 avaliações)" pequeno.
- **Páginas:** `340 páginas` em duas linhas (número grande + label).
- **Idioma:** `Português`.

> Esta linha é a versão *enxuta* da `MetricsRow` de P-03.

### 6. CTAs (Linha de Ações) — RF04.1 *(livro)*
- **Botão grande "Empréstar agora"** — `UniforPrimary`, texto branco, peso da maior parte da largura.
- **Botão secundário ícone-only:** ícone de calendário (📅) em quadrado cinza-claro à direita.
  - Provável função: agendar/reservar para data futura.

### 7. Sinopse
- Cabeçalho: **"Sinopse"** bold cor `UniforPrimary`.
- Bloco com fundo cinza-claro (`#F2F3F5`/`UniforSurfaceContainerHigh`), cantos ~16dp:
  - Texto regular cinza-escuro com line-height generoso.
  - Mock: "Esta obra explora como as tecnologias emergentes estão moldando o futuro das instituições acadêmicas e corporativas. Com uma abordagem centrada no ser humano, os autores discutem o papel da Inteligência Artificial e do Big Data na curadoria do conhecimento digital, oferecendo estratégias práticas para líderes que buscam navegar na complexidade da era moderna. Um guia essencial para estudantes e profissionais que buscam excelência no ecossistema da UNIFOR."

### 8. Informações Técnicas
Cabeçalho: **"Informações Técnicas"** bold `UniforPrimary`.

Três sub-blocos com fundo cinza-claro arredondado, cada um com *label uppercase pequeno* + *valor*:

| Label | Valor (mock) |
|-------|--------------|
| **EDITORA** | Editora Universitária |
| **ISBN** | 978-85-1234-567-8 |
| **LOCALIZAÇÃO** | Setor B, Estante 14 |

### 9. Comentários da Comunidade — RF04.2 *(livro)*
- Cabeçalho: **"Comentários da Comunidade"** bold + link **"Ver todos"** à direita (cinza/azul).
- Estrutura simplificada de comentário (RF04.2.1) com avatar quadrado de iniciais (não foto) — variante visual:
  - Avatar quadrado azul/`UniforPrimary` com **iniciais** em branco (ex.: "ML", "FR").
  - Linha 1: nome bold + estrelas amarelas + ícone de bandeira **denúncia** à direita.
  - Linha 2: texto do comentário.
  - Linha 3: botão **"Útil (12)"** com ícone polegar + tempo "Há 2 dias".

#### Exemplos no mock
| Avatar | Nome | Estrelas | Texto | Útil | Tempo |
|--------|------|----------|-------|------|-------|
| ML (azul) | Mariana Lopes | ★★★★★ | "Leitura fundamental para entender as mudanças na biblioteca digital da UNIFOR. Os autores foram muito felizes nas analogias sobre curadoria." | Útil (12) | Há 2 dias |
| FR (verde) | Felipe Rocha | ★★★★☆ | "O conteúdo é excelente, porém senti falta de mais exemplos práticos no capítulo 4. No geral, vale muito a pena o empréstimo." | Útil (4) | Há 1 semana |

### 10. BottomNav
- Itens: INÍCIO · CATÁLOGO · EMPRÉSTIMOS · PERFIL.
- Estado ativo: **CATÁLOGO** (com fundo de chip azul-claro).

✅ **Esta variante usa a BottomNav oficial conforme RF03.8** (diferente de P-02/P-03 que usavam "Alertas").

---

## Componentes reutilizáveis identificados

Adicionais aos já listados em P-03:

- `TagChipsRow` — chips outlined para categorias do livro (variante mais discreta dos `FilterChips` de P-01).
- `MetricsRowCompact` — versão de 3 métricas em linha com tipografia menor.
- `BookActionRow` — botão primary grande + ícone-only secundário ao lado.
- `TechInfoBlock` — bloco label/valor com fundo cinza-claro.
- `CommentItemCompact` — variante do `CommentItem` com avatar de iniciais + sem botão "responder" exposto.

---

## Decisão sugerida

**Adotar P-03 como tela principal** (cobre 100% dos sub-requisitos da Página do Livro, incluindo Análise de Leitura/distribuição de estrelas e tabs de ordenação). **Reutilizar de P-04** o padrão de **Sinopse**, **Informações Técnicas** e **Tags/Categorias**, adicionando-os como seções extras em P-03.

A **decisão final** deve ser confirmada com o usuário antes de implementar.
