package com.unifor.booksapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.unifor.booksapp.data.models.EmprestimoStatus

// Resposta de GET /emprestimos — endpoint exclusivo para ADMIN.
// Os campos `usuario` e `livro` são embutidos pelo backend para evitar N+1 requests no painel.
data class AdminEmprestimosResponse(
    @SerializedName("emprestimos") val emprestimos: List<AdminEmprestimo>
)

data class AdminEmprestimo(
    @SerializedName("id") val id: String,
    @SerializedName("usuarioId") val usuarioId: String,
    @SerializedName("exemplarId") val exemplarId: String,
    @SerializedName("status") val status: EmprestimoStatus,
    @SerializedName("motivoNegacao") val motivoNegacao: String? = null,
    @SerializedName("dataSolicitacao") val dataSolicitacao: String,
    @SerializedName("dataAprovacao") val dataAprovacao: String? = null,
    @SerializedName("dataDevolucaoPrevista") val dataDevolucaoPrevista: String? = null,
    @SerializedName("dataDevolucaoReal") val dataDevolucaoReal: String? = null,
    // Dados relacionais embutidos pelo backend para o painel admin
    @SerializedName("usuario") val usuario: AdminEmprestimoUsuario? = null,
    @SerializedName("livro") val livro: AdminEmprestimoLivro? = null
)

data class AdminEmprestimoUsuario(
    @SerializedName("id") val id: String,
    @SerializedName("nome") val nome: String,
    @SerializedName("email") val email: String,
    @SerializedName("matricula") val matricula: String
)

data class AdminEmprestimoLivro(
    @SerializedName("id") val id: String,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("autor") val autor: String,
    @SerializedName("capaUrl") val capaUrl: String? = null
)
