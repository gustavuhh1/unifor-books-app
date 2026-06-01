package com.unifor.booksapp.data.remote

import com.unifor.booksapp.data.models.Book
import com.unifor.booksapp.data.remote.request.LoginRequest
import com.unifor.booksapp.data.remote.request.NegarEmprestimoRequest
import com.unifor.booksapp.data.remote.request.SolicitarEmprestimoRequest
import com.unifor.booksapp.data.remote.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ── Auth ──────────────────────────────────────────────────
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(@Body body: Map<String, String>): Response<Unit>

    // ── Usuário ───────────────────────────────────────────────
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): Response<GetUserByIdResponse>

    // ── Books ─────────────────────────────────────────────────
    @GET("books")
    suspend fun getBooks(
        @Query("titulo") titulo: String? = null,
        @Query("autor") autor: String? = null,
        @Query("categoria") categoria: String? = null,
        @Query("orderBy") orderBy: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<BooksResponse>

    @GET("books/{id}")
    suspend fun getBookById(@Path("id") id: String): Response<Book>

    // ── Avaliações (ratings) ──────────────────────────────────
    @GET("books/{id}/ratings")
    suspend fun getAvaliacoes(
        @Path("id") bookId: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): Response<AvaliacoesResponse>

    // ── Empréstimos ───────────────────────────────────────────
    @POST("emprestimos/solicitar")
    suspend fun solicitarEmprestimo(
        @Body request: SolicitarEmprestimoRequest
    ): Response<SolicitarEmprestimoResponse>

    @GET("emprestimos/meus")
    suspend fun getMeusEmprestimos(): Response<MeusEmprestimosResponse>

    @PATCH("emprestimos/{id}/renovar")
    suspend fun renovarEmprestimo(@Path("id") id: String): Response<EmprestimoActionResponse>

    @PATCH("emprestimos/{id}/cancelar")
    suspend fun cancelarEmprestimo(@Path("id") id: String): Response<EmprestimoActionResponse>

    // ── Empréstimos (Admin) ───────────────────────────────────
    @GET("emprestimos")
    suspend fun getAdminEmprestimos(): Response<AdminEmprestimosResponse>

    @PATCH("emprestimos/{id}/aprovar")
    suspend fun aprovarEmprestimo(@Path("id") id: String): Response<EmprestimoActionResponse>

    @PATCH("emprestimos/{id}/negar")
    suspend fun negarEmprestimo(
        @Path("id") id: String,
        @Body body: NegarEmprestimoRequest
    ): Response<EmprestimoActionResponse>

    @PATCH("emprestimos/{id}/entregar")
    suspend fun entregarEmprestimo(@Path("id") id: String): Response<EmprestimoActionResponse>

    // ── Multas ────────────────────────────────────────────────
    @GET("multas/minhas")
    suspend fun getMinhasMultas(): Response<MultasResponse>
}
