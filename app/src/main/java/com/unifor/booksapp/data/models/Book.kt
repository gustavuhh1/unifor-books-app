package com.unifor.booksapp.data.models

import com.google.gson.annotations.SerializedName

data class Book(
    @SerializedName("id") val id: String,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("autor") val autor: String,
    @SerializedName("isbn") val isbn: String,
    @SerializedName("sinopse") val sinopse: String? = null,
    @SerializedName("capaUrl") val capaUrl: String? = null,
    @SerializedName("categoria") val categoria: String,
    @SerializedName("anoPublicacao") val anoPublicacao: Int? = null,
    @SerializedName("editora") val editora: String? = null,
    @SerializedName("idioma") val idioma: String? = null,
    @SerializedName("paginas") val paginas: Int? = null,
    @SerializedName("ativo") val ativo: Boolean = true,
    @SerializedName("criadoEm") val criadoEm: String = "",
    @SerializedName("totalExemplares") val totalExemplares: Int = 0,
    @SerializedName("exemplaresDisponiveis") val exemplaresDisponiveis: Int = 0,
    @SerializedName("mediaAvaliacao") val mediaAvaliacao: Double = 0.0,
    @SerializedName("exemplares") val exemplares: List<Exemplar>? = null
)

data class Exemplar(
    @SerializedName("id") val id: String,
    @SerializedName("numeroTombo") val numeroTombo: String,
    @SerializedName("status") val status: ExemplarStatus
)

enum class ExemplarStatus {
    @SerializedName("DISPONIVEL") DISPONIVEL,
    @SerializedName("EMPRESTADO") EMPRESTADO,
    @SerializedName("INDISPONIVEL") INDISPONIVEL
}
