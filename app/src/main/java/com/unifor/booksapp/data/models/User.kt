package com.unifor.booksapp.data.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: String,
    @SerializedName("matricula") val matricula: String,
    @SerializedName("nome") val nome: String,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: UserRole,
    @SerializedName("criadoEm") val criadoEm: String? = null
)

enum class UserRole {
    @SerializedName("ALUNO") ALUNO,
    @SerializedName("ADMIN") ADMIN
}
