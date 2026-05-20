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

@OptIn(FlowPreview::class)
class CatalogViewModel(application: Application) : AndroidViewModel(application) {

    private val bookRepository = (application as UniforBooksApp).bookRepository

    private val _uiState = MutableStateFlow<CatalogUiState>(CatalogUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _onlyAvailable = MutableStateFlow(false)
    val onlyAvailable = _onlyAvailable.asStateFlow()

    private val _topRatedOnly = MutableStateFlow(false)
    val topRatedOnly = _topRatedOnly.asStateFlow()

    // Cache local para filtros client-side após carregamento
    private var allBooks: List<Book> = emptyList()

    init {
        // Debounce na query para não chamar a API a cada tecla
        _query
            .debounce(400)
            .onEach { fetchBooks() }
            .launchIn(viewModelScope)

        fetchBooks()
    }

    fun onQueryChange(value: String) {
        _query.value = value
    }

    fun onToggleAvailable() {
        _onlyAvailable.value = !_onlyAvailable.value
        applyLocalFilter()
    }

    fun onToggleTopRated() {
        _topRatedOnly.value = !_topRatedOnly.value
        applyLocalFilter()
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
            val availableOk = if (_onlyAvailable.value) book.exemplaresDisponiveis > 0 else true
            val topRatedOk = if (_topRatedOnly.value) book.mediaAvaliacao >= 4.0 else true
            availableOk && topRatedOk
        }
        _uiState.value = CatalogUiState.Success(filtered)
    }
}
