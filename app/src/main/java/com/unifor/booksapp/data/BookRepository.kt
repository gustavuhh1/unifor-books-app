package com.unifor.booksapp.data

import com.unifor.booksapp.data.models.Book
import com.unifor.booksapp.data.remote.ApiService
import com.unifor.booksapp.data.remote.request.SolicitarEmprestimoRequest
import com.unifor.booksapp.data.remote.response.AvaliacoesResponse
import com.unifor.booksapp.data.remote.response.BooksResponse
import com.unifor.booksapp.data.remote.response.MeusEmprestimosResponse
import com.unifor.booksapp.data.remote.response.SolicitarEmprestimoResponse
import retrofit2.Response

class BookRepository(private val apiService: ApiService) {

    suspend fun getBooks(
        titulo: String? = null,
        autor: String? = null,
        categoria: String? = null,
        orderBy: String? = null,
        page: Int = 1,
        limit: Int = 20
    ): Response<BooksResponse> {
        return apiService.getBooks(titulo, autor, categoria, orderBy, page, limit)
    }

    suspend fun getBookById(id: String): Response<Book> {
        return apiService.getBookById(id)
    }

    suspend fun getAvaliacoes(bookId: String, page: Int = 1, limit: Int = 10): Response<AvaliacoesResponse> {
        return apiService.getAvaliacoes(bookId, page, limit)
    }

    suspend fun solicitarEmprestimo(livroId: String): Response<SolicitarEmprestimoResponse> {
        return apiService.solicitarEmprestimo(SolicitarEmprestimoRequest(livroId))
    }

    suspend fun getMeusEmprestimos(): Response<MeusEmprestimosResponse> {
        return apiService.getMeusEmprestimos()
    }

    suspend fun renovarEmprestimo(id: String) = apiService.renovarEmprestimo(id)

    suspend fun cancelarEmprestimo(id: String) = apiService.cancelarEmprestimo(id)
}
