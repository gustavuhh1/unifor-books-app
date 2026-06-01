package com.unifor.booksapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.unifor.booksapp.UniforBooksApp
import com.unifor.booksapp.data.models.EmprestimoStatus
import com.unifor.booksapp.data.remote.response.AdminEmprestimo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AdminLoanTab(val label: String) {
    SOLICITACOES("Solicitações"),
    RETIRADA("Retirada"),
    APROVADO("Aprovado"),
    FILA("Fila"),
    NEGADO("Negado"),
    EM_ATRASO("Em Atraso")
}

sealed class AdminLoansUiState {
    object Loading : AdminLoansUiState()
    data class Success(val totalEmprestimos: Int) : AdminLoansUiState()
    data class Error(val message: String) : AdminLoansUiState()
}

sealed class AdminActionState {
    object Idle : AdminActionState()
    data class Loading(val emprestimoId: String) : AdminActionState()
    data class Success(val message: String) : AdminActionState()
    data class Error(val message: String) : AdminActionState()
}

class AdminLoansViewModel(application: Application) : AndroidViewModel(application) {

    private val adminRepository = (application as UniforBooksApp).adminRepository

    private val _allEmprestimos = MutableStateFlow<List<AdminEmprestimo>>(emptyList())

    private val _selectedTab = MutableStateFlow(AdminLoanTab.SOLICITACOES)
    val selectedTab = _selectedTab.asStateFlow()

    private val _uiState = MutableStateFlow<AdminLoansUiState>(AdminLoansUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _actionState = MutableStateFlow<AdminActionState>(AdminActionState.Idle)
    val actionState = _actionState.asStateFlow()

    // Lista filtrada derivada da combinação de todos os empréstimos + aba selecionada.
    // "Retirada" = aprovado sem data de devolução prevista (ainda não entregue fisicamente).
    // "Aprovado"  = aprovado com data de devolução prevista (já em posse do aluno).
    val filteredEmprestimos: StateFlow<List<AdminEmprestimo>> = combine(
        _allEmprestimos, _selectedTab
    ) { all, tab ->
        when (tab) {
            AdminLoanTab.SOLICITACOES -> all.filter { it.status == EmprestimoStatus.PENDENTE }
            AdminLoanTab.RETIRADA     -> all.filter {
                it.status == EmprestimoStatus.APROVADO && it.dataDevolucaoPrevista == null
            }
            AdminLoanTab.APROVADO     -> all.filter {
                it.status == EmprestimoStatus.APROVADO && it.dataDevolucaoPrevista != null
            }
            AdminLoanTab.FILA         -> emptyList() // TODO: requer endpoint dedicado GET /fila (admin)
            AdminLoanTab.NEGADO       -> all.filter { it.status == EmprestimoStatus.NEGADO }
            AdminLoanTab.EM_ATRASO    -> all.filter { it.status == EmprestimoStatus.ATRASADO }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        fetchEmprestimos()
    }

    fun fetchEmprestimos() {
        viewModelScope.launch {
            _uiState.value = AdminLoansUiState.Loading
            try {
                val response = adminRepository.getEmprestimos()
                if (response.isSuccessful && response.body() != null) {
                    val list = response.body()!!.emprestimos
                    _allEmprestimos.value = list
                    _uiState.value = AdminLoansUiState.Success(list.size)
                } else {
                    _uiState.value = AdminLoansUiState.Error(
                        response.errorBody()?.string() ?: "Erro ao carregar empréstimos"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = AdminLoansUiState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun selectTab(tab: AdminLoanTab) {
        _selectedTab.value = tab
    }

    fun aprovar(emprestimoId: String) {
        viewModelScope.launch {
            _actionState.value = AdminActionState.Loading(emprestimoId)
            try {
                val response = adminRepository.aprovarEmprestimo(emprestimoId)
                if (response.isSuccessful) {
                    _actionState.value = AdminActionState.Success("Empréstimo aprovado com sucesso!")
                    fetchEmprestimos()
                } else {
                    _actionState.value = AdminActionState.Error("Falha ao aprovar empréstimo")
                }
            } catch (e: Exception) {
                _actionState.value = AdminActionState.Error(e.message ?: "Erro")
            }
        }
    }

    // "Cancelar" no painel admin aciona a negação formal (com motivo) — vide API PATCH /negar.
    fun negar(emprestimoId: String, motivo: String) {
        require(motivo.isNotBlank()) { "Motivo de negação não pode ser vazio" }
        viewModelScope.launch {
            _actionState.value = AdminActionState.Loading(emprestimoId)
            try {
                val response = adminRepository.negarEmprestimo(emprestimoId, motivo)
                if (response.isSuccessful) {
                    _actionState.value = AdminActionState.Success("Empréstimo negado.")
                    fetchEmprestimos()
                } else {
                    _actionState.value = AdminActionState.Error("Falha ao negar empréstimo")
                }
            } catch (e: Exception) {
                _actionState.value = AdminActionState.Error(e.message ?: "Erro")
            }
        }
    }

    fun entregar(emprestimoId: String) {
        viewModelScope.launch {
            _actionState.value = AdminActionState.Loading(emprestimoId)
            try {
                val response = adminRepository.entregarEmprestimo(emprestimoId)
                if (response.isSuccessful) {
                    _actionState.value = AdminActionState.Success("Entrega física confirmada!")
                    fetchEmprestimos()
                } else {
                    _actionState.value = AdminActionState.Error("Falha ao confirmar entrega")
                }
            } catch (e: Exception) {
                _actionState.value = AdminActionState.Error(e.message ?: "Erro")
            }
        }
    }

    fun resetActionState() {
        _actionState.value = AdminActionState.Idle
    }
}
