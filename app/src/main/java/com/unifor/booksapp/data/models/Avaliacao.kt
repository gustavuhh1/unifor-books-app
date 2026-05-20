package com.unifor.booksapp.data.models

import com.google.gson.annotations.SerializedName

data class Avaliacao(
    @SerializedName("id") val id: String,
    @SerializedName("nota") val nota: Int,
    @SerializedName("comentario") val comentario: String? = null,
    @SerializedName("criadoEm") val criadoEm: String = "",
    @SerializedName("usuario") val usuario: AvaliacaoUsuario? = null
)

data class AvaliacaoUsuario(
    @SerializedName("id") val id: String,
    @SerializedName("nome") val nome: String,
    @SerializedName("perfil") val perfil: String? = null
)
