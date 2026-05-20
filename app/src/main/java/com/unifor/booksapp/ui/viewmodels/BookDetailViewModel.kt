package com.unifor.booksapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.unifor.booksapp.UniforBooksApp
import com.unifor.booksapp.data.models.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class BookDetailUiState {
    object Loading : BookDetailUiState()
    data class Success(val book: Book) : BookDetailUiState()
    data class Error(val message: String) : BookDetailUiState()
}

sealed class EmprestimoUiState {
    object Idle : EmprestimoUiState()
    object Loading : EmprestimoUiState()
    /**
     * HTTP 201 — empréstimo criado com status PENDENTE.
     * O admin ainda precisa aprovar; o aluno entra na fila de espera de aprovação.
     */
    data class NaFila(val posicao: Int) : EmprestimoUiState()
    /** Livro sem exemplares disponíveis */
    object Indisponivel : EmprestimoUiState()
    data class Error(val message: String) : EmprestimoUiState()
}

class BookDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val bookRepository = (application as UniforBooksApp).bookRepository

    private val _bookState = MutableStateFlow<BookDetailUiState>(BookDetailUiState.Loading)
    val bookState = _bookState.asStateFlow()

    private val _emprestimoState = MutableStateFlow<EmprestimoUiState>(EmprestimoUiState.Idle)
    val emprestimoState = _emprestimoState.asStateFlow()

    fun loadBook(bookId: String) {
        viewModelScope.launch {
            _bookState.value = BookDetailUiState.Loading
            try {
                val response = bookRepository.getBookById(bookId)
                if (response.isSuccessful && response.body() != null) {
                    _bookState.value = BookDetailUiState.Success(response.body()!!)
                } else {
                    _bookState.value = BookDetailUiState.Error(
                        response.errorBody()?.string() ?: "Livro não encontrado"
                    )
                }
            } catch (e: Exception) {
                _bookState.value = BookDetailUiState.Error(e.message ?: "Erro de conexão")
            }
        }
    }

    fun solicitarEmprestimo(book: Book) {
        if (book.exemplaresDisponiveis <= 0) {
            _emprestimoState.value = EmprestimoUiState.Indisponivel
            return
        }
        viewModelScope.launch {
            _emprestimoState.value = EmprestimoUiState.Loading
            try {
                val response = bookRepository.solicitarEmprestimo(book.id)
                when {
                    // 201 = empréstimo criado com status PENDENTE (aguarda aprovação do admin)
                    // 202 = entrou na fila de espera por indisponibilidade
                    // Ambos os casos levam o aluno para a tela "Você está na fila"
                    response.isSuccessful -> {
                        val posicao = response.body()?.fila?.posicao ?: 1
                        _emprestimoState.value = EmprestimoUiState.NaFila(posicao)
                    }
                    else -> {
                        _emprestimoState.value = EmprestimoUiState.Error(
                            response.errorBody()?.string() ?: "Erro ao solicitar empréstimo"
                        )
                    }
                }
            } catch (e: Exception) {
                _emprestimoState.value = EmprestimoUiState.Error(e.message ?: "Erro de conexão")
            }
        }
    }

    fun resetEmprestimoState() {
        _emprestimoState.value = EmprestimoUiState.Idle
    }
}
