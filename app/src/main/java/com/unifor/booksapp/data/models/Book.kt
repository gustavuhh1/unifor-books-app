package com.unifor.booksapp.data.models

import com.google.gson.annotations.SerializedName

data class Book(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("titulo") val titulo: String,
    @field:SerializedName("autor") val autor: String,
    @field:SerializedName("isbn") val isbn: String,
    @field:SerializedName("sinopse") val sinopse: String,
    @field:SerializedName("capaUrl") val capaUrl: String,
    @field:SerializedName("categoria") val categoria: String,
    @field:SerializedName("anoPublicacao") val anoPublicacao: Int,
    @field:SerializedName("editora") val editora: String,
    @field:SerializedName("idioma") val idioma: String,
    @field:SerializedName("paginas") val paginas: Int,
    @field:SerializedName("ativo") val ativo: Boolean,
    @field:SerializedName("criadoEm") val criadoEm: String,
    @field:SerializedName("totalExemplares") val totalExemplares: Int,
    @field:SerializedName("exemplaresDisponiveis") val exemplaresDisponiveis: Int,
    @field:SerializedName("mediaAvaliacao") val mediaAvaliacao: Double
)
