package com.unifor.booksapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.unifor.booksapp.UniforBooksApp
import com.unifor.booksapp.data.models.Avaliacao
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
    data class NaFila(val posicao: Int) : EmprestimoUiState()
    object Indisponivel : EmprestimoUiState()
    data class Error(val message: String) : EmprestimoUiState()
}

sealed class AvaliacoesUiState {
    object Loading : AvaliacoesUiState()
    data class Success(
        val avaliacoes: List<Avaliacao>,
        val total: Int,
        val media: Double
    ) : AvaliacoesUiState()
    object Error : AvaliacoesUiState()
}

class BookDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val bookRepository = (application as UniforBooksApp).bookRepository

    private val _bookState = MutableStateFlow<BookDetailUiState>(BookDetailUiState.Loading)
    val bookState = _bookState.asStateFlow()

    private val _emprestimoState = MutableStateFlow<EmprestimoUiState>(EmprestimoUiState.Idle)
    val emprestimoState = _emprestimoState.asStateFlow()

    private val _avaliacoesState = MutableStateFlow<AvaliacoesUiState>(AvaliacoesUiState.Loading)
    val avaliacoesState = _avaliacoesState.asStateFlow()

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
        loadAvaliacoes(bookId)
    }

    private fun loadAvaliacoes(bookId: String) {
        viewModelScope.launch {
            _avaliacoesState.value = AvaliacoesUiState.Loading
            try {
                val response = bookRepository.getAvaliacoes(bookId, limit = 5)
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    _avaliacoesState.value = AvaliacoesUiState.Success(
                        avaliacoes = body.avaliacoes,
                        total = body.total,
                        media = body.media
                    )
                } else {
                    _avaliacoesState.value = AvaliacoesUiState.Error
                }
            } catch (e: Exception) {
                _avaliacoesState.value = AvaliacoesUiState.Error
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
