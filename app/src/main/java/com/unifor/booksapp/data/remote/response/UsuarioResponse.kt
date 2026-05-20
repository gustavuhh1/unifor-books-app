package com.unifor.booksapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.unifor.booksapp.data.models.UserRole

data class UsuarioResponse(
    @SerializedName("id") val id: String,
    @SerializedName("matricula") val matricula: String,
    @SerializedName("nome") val nome: String,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: UserRole,
    @SerializedName("criadoEm") val criadoEm: String? = null
)
