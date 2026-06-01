package com.unifor.booksapp.data.models

import com.google.gson.annotations.SerializedName

data class EmprestimoComLivro(
    val emprestimo: Emprestimo,
    val livro: Book? = null
)

data class Emprestimo(
    @SerializedName("id") val id: String,
    @SerializedName("usuarioId") val usuarioId: String,
    @SerializedName("exemplarId") val exemplarId: String,
    @SerializedName("status") val status: EmprestimoStatus,
    @SerializedName("motivoNegacao") val motivoNegacao: String? = null,
    @SerializedName("dataSolicitacao") val dataSolicitacao: String,
    @SerializedName("dataAprovacao") val dataAprovacao: String? = null,
    @SerializedName("dataDevolucaoPrevista") val dataDevolucaoPrevista: String? = null,
    @SerializedName("dataDevolucaoReal") val dataDevolucaoReal: String? = null
)

enum class EmprestimoStatus {
    @SerializedName("PENDENTE") PENDENTE,
    @SerializedName("APROVADO") APROVADO,
    @SerializedName("NEGADO") NEGADO,
    @SerializedName("DEVOLVIDO") DEVOLVIDO,
    @SerializedName("CANCELADO") CANCELADO,
    @SerializedName("ATRASADO") ATRASADO
}

data class FilaEspera(
    @SerializedName("id") val id: String,
    @SerializedName("usuarioId") val usuarioId: String,
    @SerializedName("livroId") val livroId: String,
    @SerializedName("posicao") val posicao: Int,
    @SerializedName("status") val status: String
)

data class Multa(
    @SerializedName("id") val id: String,
    @SerializedName("emprestimoId") val emprestimoId: String,
    @SerializedName("usuarioId") val usuarioId: String,
    @SerializedName("valorTotal") val valorTotal: Double,
    @SerializedName("diasAtraso") val diasAtraso: Int,
    @SerializedName("status") val status: String,
    @SerializedName("dataPagamento") val dataPagamento: String? = null,
    @SerializedName("criadoEm") val criadoEm: String
)
