package com.unifor.booksapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.unifor.booksapp.data.models.Emprestimo
import com.unifor.booksapp.data.models.FilaEspera
import com.unifor.booksapp.data.models.Multa

// Resposta de solicitação: 201 = empréstimo direto, 202 = entrou na fila
data class SolicitarEmprestimoResponse(
    @SerializedName("message") val message: String,
    @SerializedName("emprestimo") val emprestimo: Emprestimo? = null,
    @SerializedName("fila") val fila: FilaEspera? = null
)

data class MeusEmprestimosResponse(
    @SerializedName("emprestimos") val emprestimos: List<Emprestimo>,
    @SerializedName("filaEspera") val filaEspera: List<FilaEspera>
)

data class EmprestimoActionResponse(
    @SerializedName("message") val message: String,
    @SerializedName("emprestimo") val emprestimo: Emprestimo
)

data class MultasResponse(
    @SerializedName("multas") val multas: List<Multa>
)

data class MultaActionResponse(
    @SerializedName("message") val message: String,
    @SerializedName("multa") val multa: Multa
)
