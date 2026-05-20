# P-03 — Página do Livro (versão completa com avaliações + comentários)

**Tela alvo:** Página do Livro / Visualização de livro (Issue #6)
**Cobre os requisitos:** RF04 *(livro)*, RF04.05, RF04.1 *(livro)*, RF04.2 *(livro)*, RF04.2.1, RF04.3 *(livro)*, RF04.4 *(livro)*, RF04.5 *(livro)*, RF04.6, RF04.7, RF04.8

---

## Layout (top → bottom)

### 1. TopBar
- **Esquerda:** ícone de **voltar** (chevron `<`).
- **Centro:** texto **"UNIFOR"** em fonte bold `UniforPrimary`.
- **Direita:** avatar circular do usuário.

### 2. Hero do Livro
- **Capa do livro grande**, ratio ~3:4, centralizada horizontalmente, fundo escuro/contrastante (a do mock está em fundo preto/cinza-escuro). Cantos arredondados leves (~16dp).
- **Badge "DISPONÍVEL"** sobreposto no canto superior esquerdo da capa: fundo `UniforSecondaryContainer`, texto `UniforSecondary`, fonte black uppercase.

### 3. Título e Autor
- **Título grande:** "A Arquitetura da Informação Contemporânea" — fonte black ~26sp, cor `UniforPrimary`, com possível wrap em duas linhas.
- **Autor:** "Dr. Ricardo Vasconcelos" — fonte regular, cor cinza/`UniforOutline`.

### 4. Métricas (linha horizontal de 3 colunas) — RF04.05
Cada coluna tem rótulo pequeno em cima + valor abaixo:

| Métrica | Valor (mock) | Visual |
|---------|--------------|--------|
| **Avaliação média** | `4.5 / 5.0` com **estrelas amarelas** ao lado | estrelas + nota |
| **AVALIAÇÕES** | `128` | número grande + label uppercase |
| **PÁGINAS** | `432` | número grande + label uppercase |

Separadores verticais finos entre as colunas (cor cinza muito clara).

### 5. Descrição da Obra
- Cabeçalho: "Descrição da Obra".
- Texto: parágrafo em cor `UniforOutline`/cinza-médio, ~14sp, line-height generoso.
- Mock: "Uma exploração profunda sobre como os sistemas digitais moldam nossa percepção da realidade acadêmica. Este volume premiado pela UNIFOR Press oferece uma visão crítica sobre as estruturas de dados no contexto bibliográfico moderno, servindo como guia essencial para estudantes de Design e Ciência da Computação."

### 6. CTA Primary — RF04.1 *(livro)*
- **Botão "Solicitar Empréstimo"** — fundo `UniforPrimary`, texto branco, largura cheia, cantos ~12dp.

### 7. Análise de Leitura — RF04.05
Sub-card com:
- Cabeçalho: **"Análise de Leitura"** bold.
- À esquerda: **nota gigante** "4.5" + label "Média baseada em 128 leitores".
- À direita: ícone de estrela amarelo grande (como destaque visual).
- **Distribuição por estrelas** (5 linhas, da nota 5 para 2):
  - 5 ★ — barra verde cheia 75%
  - 4 ★ — barra verde 15%
  - 3 ★ — barra amarela 8%
  - 2 ★ — barra rosa-clara 2%
  - *(linha de 1 ★ não aparece visível no recorte; possivelmente 0%)*

### 8. Bloco "Escolha da Equipe" (curadoria)
- Card horizontal de fundo dourado/marrom-amarelado (`UniforTertiaryFixed` aplicado escuro).
- Ícone de livro à esquerda.
- Título: **"Escolha da Equipe"** branco bold.
- Texto pequeno em itálico/quote: "Essencial para o currículo de Arquitetura de Dados da UNIFOR."

### 9. Comentários da Comunidade — RF04.2 *(livro)* + RF04.6
- Cabeçalho: **"Comentários da Comunidade"** bold.
- **Tabs/segmented** horizontais para ordenação (RF04.6):
  - **"Mais Relevantes"** *(ativo, sublinhado/escuro)*
  - **"Mais Recentes"**
  - **"M…"** *(corte na imagem, presumido "Mais Curtidas")*

#### Estrutura de cada comentário (RF04.2.1)
| Elemento | Descrição |
|----------|-----------|
| Avatar circular | Foto de perfil do autor |
| Nome do autor | Bold, ex.: "Beatriz Menezes" |
| Estrelas | Linha de 5 estrelas amarelas (avaliação dada) |
| Sub-info | "Estudante de Engenharia • Ontem" — cinza pequeno |
| Texto do comentário | Parágrafo regular |
| Botão **curtir** | Pill verde-clara com ícone de polegar + número (ex.: "👍 24") *quando o usuário já curtiu* |
| Botão **responder** | Texto-link "Responder" cinza |
| Botão **denunciar** | Ícone de bandeira (RF04.7) — não desenhado explicitamente, mas implícito pelo requisito |
| Texto "Há há tempo" | Já incluído no sub-info |

#### Exemplos de comentários no mock
| Autor | Sub-info | Curtidas | Texto |
|-------|----------|----------|-------|
| Beatriz Menezes | Estudante de Engenharia • Ontem | 24 (curtido pelo usuário) | "Leitura obrigatória para quem entender o futuro das bibliotecas digitais. A linguagem é técnica mas muito acessível. Recomendo fortemente para os alunos do primeiro semestre." |
| Prof. Marcos André | Docente • Há 3 dias | 12 (não-curtido) | "O capitulo sobre ontologias é o ponto alto. Embora o livro tenha sido escrito há dois anos, os conceitos permanecem extremamente atuais dentro do nosso campus." |

### 10. CTA Secundário
- Botão **"Ver mais 126 comentários"** — fundo cinza-claro, texto cor `onSurface`, largura cheia.

### 11. BottomNav
- Estado ativo: **LOANS** *(no mock — também aparece em outras telas como "EMPRÉSTIMOS")*.
- Itens vistos: HOME · LOANS · ALERTS · PROFILE.
- ⚠️ Mesma inconsistência de P-02: o mock usa "LOANS/ALERTS/PROFILE" em inglês e a aba "ALERTS" não bate com RF03.8. **Padronizar** durante a implementação (Início · Catálogo · Empréstimos · Perfil).

---

## Comportamento esperado

- Tap em **"Solicitar Empréstimo"** → registra solicitação (RF05.1.1) e redireciona para "Empréstimos" (RF05.2). Estado: `Pendente` (RF05.3). Pode disparar o feedback de P-02 cenário 2 caso indisponível.
- Tap em **estrelas vazias** *(em algum ponto da tela; não está no mock mas é exigido)* → aluno avalia o livro com 0.5–5 ★ (RF04.3 *livro*).
- Tap em **bandeira de denúncia** em um comentário → navega para "Denunciar Comentário" (RF04.7, RF04.8) → P-07 *(a criar)*.
- Tap em **"Ver mais 126 comentários"** → tela de listagem completa de comentários (Issue #8 — Tela de Comentários).
- Tap em **tab de ordenação** → reordena lista (RF04.6).
- Tap em **curtir** → toggle do polegar (RF04.5 *livro*).

## Componentes reutilizáveis identificados

- `BookHeroCover` — capa grande com badge sobreposto.
- `MetricsRow` — linha de 3 métricas com separadores verticais.
- `RatingDistributionBars` — gráfico de barras horizontal por nota (5★→1★).
- `PickedByTeamCard` — card destaque dourado.
- `CommentSortTabs` — tabs/segmented para ordenação.
- `CommentItem` — card de comentário com todos os elementos do RF04.2.1.
  - Subcomponente `LikeChip` (estado curtido/não-curtido).
  - Subcomponente `ReportFlagButton`.

---

## Pendências de design a confirmar

1. **Onde** exatamente o usuário avalia o livro com estrelas? (RF04.3 *livro*) — não há um controle de estrelas interativo desenhado explicitamente. Sugestão: um bloco "Sua avaliação" com estrelas tocáveis acima ou junto da Análise de Leitura.
2. **Caixa de envio de comentário** (RF04.4 *livro*) — não desenhada explicitamente. Sugestão: input de texto fixo no rodapé da seção de comentários ou modal/bottom-sheet.
3. **Bandeira de denúncia** em cada comentário (RF04.2.1) — não visível nas duas amostras de comentário. Será adicionada como ícone à direita do header do comentário.
4. **Botão "responder"** (RF04.2.1) — visível como link, mas o **fluxo de resposta** (thread) não está desenhado.
