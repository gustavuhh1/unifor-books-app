package com.unifor.booksapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.unifor.booksapp.UniforBooksApp
import com.unifor.booksapp.data.models.Book
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class CatalogUiState {
    object Loading : CatalogUiState()
    data class Success(val books: List<Book>) : CatalogUiState()
    data class Error(val message: String) : CatalogUiState()
}

enum class CatalogFilter { TODOS, MAIOR_AVALIACAO, DISPONIVEIS }

@OptIn(FlowPreview::class)
class CatalogViewModel(application: Application) : AndroidViewModel(application) {

    private val bookRepository = (application as UniforBooksApp).bookRepository

    private val _uiState = MutableStateFlow<CatalogUiState>(CatalogUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _activeFilter = MutableStateFlow(CatalogFilter.TODOS)
    val activeFilter = _activeFilter.asStateFlow()

    // Mantidos para compatibilidade com código existente
    val onlyAvailable = _activeFilter.map { it == CatalogFilter.DISPONIVEIS }.stateIn(
        viewModelScope, SharingStarted.Eagerly, false
    )
    val topRatedOnly = _activeFilter.map { it == CatalogFilter.MAIOR_AVALIACAO }.stateIn(
        viewModelScope, SharingStarted.Eagerly, false
    )

    private var allBooks: List<Book> = emptyList()

    init {
        _query
            .debounce(400)
            .onEach { fetchBooks() }
            .launchIn(viewModelScope)

        fetchBooks()
    }

    fun onQueryChange(value: String) {
        _query.value = value
    }

    fun onFilterChange(filter: CatalogFilter) {
        _activeFilter.value = filter
        applyLocalFilter()
    }

    fun onToggleAvailable() {
        onFilterChange(
            if (_activeFilter.value == CatalogFilter.DISPONIVEIS) CatalogFilter.TODOS
            else CatalogFilter.DISPONIVEIS
        )
    }

    fun onToggleTopRated() {
        onFilterChange(
            if (_activeFilter.value == CatalogFilter.MAIOR_AVALIACAO) CatalogFilter.TODOS
            else CatalogFilter.MAIOR_AVALIACAO
        )
    }

    fun fetchBooks() {
        viewModelScope.launch {
            _uiState.value = CatalogUiState.Loading
            try {
                val titulo = _query.value.takeIf { it.isNotBlank() }
                val response = bookRepository.getBooks(titulo = titulo, limit = 50)
                if (response.isSuccessful && response.body() != null) {
                    allBooks = response.body()!!.data
                    applyLocalFilter()
                } else {
                    _uiState.value = CatalogUiState.Error(
                        response.errorBody()?.string() ?: "Erro ao carregar catálogo"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = CatalogUiState.Error(e.message ?: "Erro de conexão")
            }
        }
    }

    private fun applyLocalFilter() {
        val filtered = allBooks.filter { book ->
            when (_activeFilter.value) {
                CatalogFilter.TODOS -> true
                CatalogFilter.DISPONIVEIS -> book.exemplaresDisponiveis > 0
                CatalogFilter.MAIOR_AVALIACAO -> book.mediaAvaliacao >= 4.0
            }
        }
        _uiState.value = CatalogUiState.Success(filtered)
    }
}
