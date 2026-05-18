package com.unifor.booksapp.data

import com.unifor.booksapp.data.remote.ApiService
import com.unifor.booksapp.data.remote.RetrofitClient
import com.unifor.booksapp.data.remote.response.BooksResponse
import retrofit2.Response

class BookRepository(
    private val apiService: ApiService = RetrofitClient.instance
) {

    suspend fun getBooks(): Response<BooksResponse> {
        return apiService.getBooks()
    }
}
