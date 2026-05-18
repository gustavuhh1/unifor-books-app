package com.unifor.booksapp.data.remote

import com.unifor.booksapp.data.remote.response.BooksResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    // Endpoints começarão a ser adicionados aqui, a partir da Home.

    @GET("books")
    suspend fun getBooks(): Response<BooksResponse>
}
