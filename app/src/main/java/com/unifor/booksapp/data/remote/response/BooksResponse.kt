package com.unifor.booksapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.unifor.booksapp.data.models.Book

data class BooksResponse(
    @field:SerializedName("data") val data: List<Book>,
    @field:SerializedName("total") val total: Int,
    @field:SerializedName("page") val page: Int,
    @field:SerializedName("limit") val limit: Int
)
