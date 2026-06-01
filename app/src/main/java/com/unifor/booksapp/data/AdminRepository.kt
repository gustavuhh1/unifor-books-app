package com.unifor.booksapp.data

import com.unifor.booksapp.data.remote.ApiService
import com.unifor.booksapp.data.remote.request.NegarEmprestimoRequest
import com.unifor.booksapp.data.remote.response.AdminEmprestimosResponse
import com.unifor.booksapp.data.remote.response.EmprestimoActionResponse
import retrofit2.Response

class AdminRepository(private val api: ApiService) {

    suspend fun getEmprestimos(): Response<AdminEmprestimosResponse> =
        api.getAdminEmprestimos()

    suspend fun aprovarEmprestimo(id: String): Response<EmprestimoActionResponse> =
        api.aprovarEmprestimo(id)

    suspend fun negarEmprestimo(id: String, motivo: String): Response<EmprestimoActionResponse> =
        api.negarEmprestimo(id, NegarEmprestimoRequest(motivo))

    suspend fun entregarEmprestimo(id: String): Response<EmprestimoActionResponse> =
        api.entregarEmprestimo(id)

    suspend fun cancelarEmprestimo(id: String): Response<EmprestimoActionResponse> =
        api.cancelarEmprestimo(id)
}
