package com.unifor.booksapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.unifor.booksapp.data.models.Book

data class BooksResponse(
    @SerializedName("data") val data: List<Book>,
    @SerializedName("total") val total: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("limit") val limit: Int
)
