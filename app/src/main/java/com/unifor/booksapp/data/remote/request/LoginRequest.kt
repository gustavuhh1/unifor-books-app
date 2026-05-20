package com.unifor.booksapp.data.remote.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("matricula") val matricula: String,
    @SerializedName("senha") val senha: String
)
