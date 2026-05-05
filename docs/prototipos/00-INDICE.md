# Índice de Protótipos de Alta Fidelidade

| ID | Tela | Arquivo |
|----|------|---------|
| **P-01** | Catálogo de livros | [P01-catalogo.md](./P01-catalogo.md) |
| **P-02** | Feedback de Empréstimo (Aprovado / Indisponível) | [P02-emprestimo-feedback.md](./P02-emprestimo-feedback.md) |
| **P-03** | Página do Livro — versão completa (com avaliações + comentários) | [P03-pagina-livro.md](./P03-pagina-livro.md) |
| **P-04** | Detalhes do Livro — versão "Empréstar agora" | [P04-detalhes-livro.md](./P04-detalhes-livro.md) |
| **P-05** | Meus Empréstimos | [P05-meus-emprestimos.md](./P05-meus-emprestimos.md) |
| **P-06** | Guia de Política de Multas | [P06-guia-multas.md](./P06-guia-multas.md) |

## Convenções

- **Cores referenciadas** seguem a paleta já presente em `ui/theme/Color.kt`:
  - `UniforPrimary` = `#00346F` (azul-marinho institucional)
  - `UniforSecondary` = `#006D35` (verde Unifor)
  - `UniforSecondaryContainer` = `#8AFAA7` (verde-claro do badge "DISPONÍVEL")
  - `UniforTertiaryFixed` = `#FEBB2B` (amarelo das estrelas)
  - `UniforSurfaceContainerHigh` = `#E7E8E9` (cinza-claro de cards)

- Quando o protótipo introduz uma cor não-mapeada (ex.: vermelho de "ATRASADO" ou "ERR_403"), ela está marcada como `[NOVA-CORn]` na descrição e deverá ser adicionada ao `Color.kt` durante a implementação.

- Identificadores de protótipo **P-xx** são internos a esta documentação; os nomes oficiais das telas seguem `LISTA-TELAS.md`.
