package com.unifor.booksapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.unifor.booksapp.UniforBooksApp
import com.unifor.booksapp.data.models.Book
import com.unifor.booksapp.data.models.Emprestimo
import com.unifor.booksapp.data.models.EmprestimoComLivro
import com.unifor.booksapp.data.models.EmprestimoStatus
import com.unifor.booksapp.data.models.FilaEspera
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MyLoansData(
    val emprestimos: List<EmprestimoComLivro>,
    val filaEspera: List<FilaEspera>,
    val booksMap: Map<String, Book> = emptyMap()
)

sealed class MyLoansUiState {
    object Loading : MyLoansUiState()
    data class Success(val data: MyLoansData) : MyLoansUiState()
    data class Error(val message: String) : MyLoansUiState()
}

sealed class RenovacaoUiState {
    object Idle : RenovacaoUiState()
    object Loading : RenovacaoUiState()
    object Sucesso : RenovacaoUiState()
    object Indisponivel : RenovacaoUiState()
    data class Error(val message: String) : RenovacaoUiState()
}

class MyLoansViewModel(application: Application) : AndroidViewModel(application) {

    private val bookRepository = (application as UniforBooksApp).bookRepository

    private val _uiState = MutableStateFlow<MyLoansUiState>(MyLoansUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _renovacaoState = MutableStateFlow<RenovacaoUiState>(RenovacaoUiState.Idle)
    val renovacaoState = _renovacaoState.asStateFlow()

    // fetchLoans() é chamado pela tela via repeatOnLifecycle(RESUMED),
    // garantindo que a lista sempre reflita o estado mais recente ao entrar na tela.
    fun fetchLoans() {
        viewModelScope.launch {
            _uiState.value = MyLoansUiState.Loading
            try {
                val loansResponse = bookRepository.getMeusEmprestimos()
                if (!loansResponse.isSuccessful || loansResponse.body() == null) {
                    _uiState.value = MyLoansUiState.Error(
                        loansResponse.errorBody()?.string() ?: "Erro ao carregar empréstimos"
                    )
                    return@launch
                }

                val body = loansResponse.body()!!
                val emprestimos: List<Emprestimo> = body.emprestimos
                val filaEspera: List<FilaEspera> = body.filaEspera

                // Busca catálogo para enriquecer empréstimos e fila de espera
                val booksResponse = bookRepository.getBooks(limit = 100)
                val allBooks: List<Book> = booksResponse.body()?.data ?: emptyList()
                val booksById: Map<String, Book> = allBooks.associateBy { it.id }

                // Busca detalhes completos (com exemplares) apenas para livros com cópias emprestadas
                val booksWithLentCopies = allBooks.filter { it.exemplaresDisponiveis < it.totalExemplares }
                val exemplarToBook: MutableMap<String, Book> = mutableMapOf()
                coroutineScope {
                    booksWithLentCopies.map { book ->
                        async {
                            runCatching { bookRepository.getBookById(book.id).body() }
                                .getOrNull()
                        }
                    }.awaitAll().forEach { bookDetail ->
                        bookDetail?.exemplares?.forEach { exemplar ->
                            exemplarToBook[exemplar.id] = bookDetail
                        }
                    }
                }

                val enrichedLoans = emprestimos.map { emprestimo ->
                    EmprestimoComLivro(
                        emprestimo = emprestimo,
                        livro = exemplarToBook[emprestimo.exemplarId]
                    )
                }

                _uiState.value = MyLoansUiState.Success(
                    MyLoansData(
                        emprestimos = enrichedLoans,
                        filaEspera = filaEspera,
                        booksMap = booksById
                    )
                )
            } catch (e: Exception) {
                _uiState.value = MyLoansUiState.Error(e.message ?: "Erro de conexão")
            }
        }
    }

    fun renovarEmprestimo(emprestimoId: String) {
        viewModelScope.launch {
            _renovacaoState.value = RenovacaoUiState.Loading
            try {
                val response = bookRepository.renovarEmprestimo(emprestimoId)
                if (response.isSuccessful) {
                    _renovacaoState.value = RenovacaoUiState.Sucesso
                    fetchLoans()
                } else {
                    if (response.code() == 409) {
                        _renovacaoState.value = RenovacaoUiState.Indisponivel
                    } else {
                        _renovacaoState.value = RenovacaoUiState.Error(
                            response.errorBody()?.string() ?: "Erro ao renovar"
                        )
                    }
                }
            } catch (e: Exception) {
                _renovacaoState.value = RenovacaoUiState.Error(e.message ?: "Erro de conexão")
            }
        }
    }

    fun resetRenovacaoState() {
        _renovacaoState.value = RenovacaoUiState.Idle
    }
}
