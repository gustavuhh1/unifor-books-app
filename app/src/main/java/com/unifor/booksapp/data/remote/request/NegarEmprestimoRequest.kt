package com.unifor.booksapp.data.remote.request

import com.google.gson.annotations.SerializedName

data class NegarEmprestimoRequest(
    @SerializedName("motivoNegacao") val motivoNegacao: String
)
