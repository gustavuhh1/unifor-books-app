package com.unifor.booksapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.unifor.booksapp.data.models.User

data class LoginResponse(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("usuario") val usuario: User
)
