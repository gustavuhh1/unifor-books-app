package com.unifor.booksapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.unifor.booksapp.data.models.Avaliacao

data class AvaliacoesResponse(
    @SerializedName("avaliacoes") val avaliacoes: List<Avaliacao>,
    @SerializedName("total") val total: Int = 0,
    @SerializedName("media") val media: Double = 0.0
)
