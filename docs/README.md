# Documentação — Unifor Books App

Esta pasta contém **toda a documentação de origem** do projeto Unifor Books App, transcrita a partir do material entregue pelo cliente/disciplina (planilha de requisitos funcionais, lista de issues do GitHub e protótipos de alta fidelidade).

> ⚠️ **Esta documentação é a fonte da verdade do projeto.**
> Toda implementação posterior deve referenciar os códigos `RFxx.x` e os identificadores de protótipo (`P-xx`) descritos aqui.

---

## Estrutura

```
docs/
├── README.md                       ← este arquivo
├── requisitos/
│   ├── REQUISITOS-ALUNO.md         RFs do perfil Aluno (RF01..RF8)
│   └── REQUISITOS-ADMIN.md         RFs do perfil Administrador
├── prototipos/
│   ├── 00-INDICE.md                Índice de protótipos
│   ├── P01-catalogo.md
│   ├── P02-emprestimo-feedback.md  (Aprovado / Indisponível)
│   ├── P03-pagina-livro.md         (com avaliações + comentários)
│   ├── P04-detalhes-livro.md       (versão "Empréstar agora")
│   ├── P05-meus-emprestimos.md
│   └── P06-guia-multas.md
├── issues/
│   └── LISTA-TELAS.md              Issues #5..#18 do repositório
└── verificacao/
    └── TRIPLE-CHECK.md             Relatório do Harness Architecture
```

---

## Harness Architecture (Triple Check)

A documentação passa por **três passagens independentes** de verificação antes de ser considerada congelada:

| Passe | Objetivo | Entregável |
|-------|----------|------------|
| **Pass 1 — Transcrição** | Releitura linha-a-linha confirmando que cada item documentado corresponde literalmente ao material original. | Marcação ✅/❌ por item. |
| **Pass 2 — Cross-validation** | Comparação cruzada entre documentação e fontes (imagens da planilha, screenshots dos protótipos, títulos das issues). | Tabela de matches. |
| **Pass 3 — Auditoria de completude** | Verifica omissões: cada elemento do material original aparece na documentação? | Score de fidelidade + relatório. |

**Critério de aceite:** fidelidade ≥ **95%**.
Caso contrário, retornar à passagem que falhou e corrigir antes de seguir para implementação.

Relatório consolidado em [`verificacao/TRIPLE-CHECK.md`](./verificacao/TRIPLE-CHECK.md).

---

## Convenções

- **Códigos de requisito** (`RF01.0`, `RF04.10`, `RNF04.3`) seguem **exatamente** a planilha original. Duplicações de código (ex.: `RF04` aparece duas vezes para o Aluno) são preservadas e anotadas como tal.
- **Identificadores de protótipo** (`P-01`, `P-02`, …) são criados nesta documentação para referência cruzada.
- **Telas pendentes** (issues do GitHub) usam o número da issue (`#5`, `#6`, …).

---

## Status

| Etapa | Status |
|-------|--------|
| Estrutura de pastas | ⏳ em criação |
| Lista de Telas (issues) | ⏳ pendente |
| Requisitos — Aluno | ⏳ pendente |
| Requisitos — Admin | ⏳ pendente |
| Protótipos | ⏳ pendente |
| Triple Check Pass 1 | ⏳ pendente |
| Triple Check Pass 2 | ⏳ pendente |
| Triple Check Pass 3 | ⏳ pendente |
