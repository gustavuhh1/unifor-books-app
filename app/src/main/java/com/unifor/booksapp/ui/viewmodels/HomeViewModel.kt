package com.unifor.booksapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.unifor.booksapp.UniforBooksApp
import com.unifor.booksapp.data.models.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val books: List<Book>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val bookRepository = (application as UniforBooksApp).bookRepository

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchBooks()
    }

    fun fetchBooks() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val response = bookRepository.getBooks()
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = HomeUiState.Success(response.body()!!.data)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Erro ao buscar livros"
                    _uiState.value = HomeUiState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Erro de conexão")
            }
        }
    }
}
