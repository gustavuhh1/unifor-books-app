package com.unifor.booksapp.data.remote.request

import com.google.gson.annotations.SerializedName

data class SolicitarEmprestimoRequest(
    @SerializedName("livroId") val livroId: String
)
