package com.unifor.booksapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.unifor.booksapp.data.models.EmprestimoStatus
import com.unifor.booksapp.data.remote.response.AdminEmprestimo
import com.unifor.booksapp.data.remote.response.AdminEmprestimoMulta
import com.unifor.booksapp.data.remote.response.AdminEmprestimoUsuario
import com.unifor.booksapp.ui.theme.*
import com.unifor.booksapp.ui.viewmodels.*
import java.text.SimpleDateFormat
import java.util.*

private val SubtitleColor = Color(0xFF424751)
private val BadgePendenteBg = Color(0x4D939393)
private val BadgePendenteText = Color(0xFFA0A0A0)
private val BadgeEmAndamentoBg = Color(0xFFE8F0FE)
private val BadgeEmAndamentoText = Color(0xFF00346F)

// ─────────────────────────────────────────────────────────────────────────────
// Entry point
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminLoansPanelScreen(
    onBack: () -> Unit,
    viewModel: AdminLoansViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val filteredList by viewModel.filteredEmprestimos.collectAsState()
    val actionState by viewModel.actionState.collectAsState()
    val totalMulta by viewModel.totalMultaPendente.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var negacaoDialogId by remember { mutableStateOf<String?>(null) }
    var negacaoMotivo by remember { mutableStateOf("") }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.fetchEmprestimos()
        }
    }

    LaunchedEffect(actionState) {
        when (actionState) {
            is AdminActionState.Success -> {
                snackbarHostState.showSnackbar((actionState as AdminActionState.Success).message)
                viewModel.resetActionState()
            }
            is AdminActionState.Error -> {
                snackbarHostState.showSnackbar((actionState as AdminActionState.Error).message)
                viewModel.resetActionState()
            }
            else -> Unit
        }
    }

    NegacaoDialog(
        emprestimoId = negacaoDialogId,
        motivo = negacaoMotivo,
        onMotivoChange = { negacaoMotivo = it },
        onConfirm = { id ->
            viewModel.negar(id, negacaoMotivo)
            negacaoDialogId = null
            negacaoMotivo = ""
        },
        onDismiss = { negacaoDialogId = null; negacaoMotivo = "" }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Unifor Books Admin",
                        fontWeight = FontWeight.ExtraBold,
                        color = UniforPrimary,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = UniforPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = UniforSurface)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = UniforBackground
    ) { padding ->
        when (val state = uiState) {
            is AdminLoansUiState.Loading -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = UniforPrimary)
                }
            }
            is AdminLoansUiState.Error -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(state.message, color = UniforError, fontWeight = FontWeight.Bold)
                        TextButton(onClick = viewModel::fetchEmprestimos) {
                            Text("Tentar novamente", color = UniforPrimary)
                        }
                    }
                }
            }
            is AdminLoansUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { AdminPainelHeader() }
                    item {
                        AdminFilterTabs(selectedTab = selectedTab, onTabSelected = viewModel::selectTab)
                    }
                    item {
                        AdminSummaryCard(
                            count = filteredList.size,
                            tab = selectedTab,
                            totalMulta = totalMulta
                        )
                    }
                    if (filteredList.isEmpty()) {
                        item { AdminEmptyState(selectedTab) }
                    } else {
                        items(filteredList, key = { it.id }) { emprestimo ->
                            val isActionLoading = actionState is AdminActionState.Loading &&
                                (actionState as AdminActionState.Loading).emprestimoId == emprestimo.id
                            AdminLoanCard(
                                emprestimo = emprestimo,
                                tab = selectedTab,
                                isLoading = isActionLoading,
                                onAprovar = { viewModel.aprovar(emprestimo.id) },
                                onCancelar = { negacaoDialogId = emprestimo.id },
                                onEntregar = { viewModel.entregar(emprestimo.id) },
                                onDevolver = { viewModel.devolver(emprestimo.id) },
                                onQuitarMulta = {
                                    emprestimo.multa?.id?.let { viewModel.quitarMulta(it) }
                                }
                            )
                        }
                    }
                    item { Spacer(Modifier.height(8.dp)) }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Dialogs
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun NegacaoDialog(
    emprestimoId: String?,
    motivo: String,
    onMotivoChange: (String) -> Unit,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    emprestimoId ?: return
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Motivo da Negação", fontWeight = FontWeight.Bold, color = UniforPrimary) },
        text = {
            OutlinedTextField(
                value = motivo,
                onValueChange = onMotivoChange,
                placeholder = { Text("Descreva o motivo...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4
            )
        },
        confirmButton = {
            TextButton(
                onClick = { if (motivo.isNotBlank()) onConfirm(emprestimoId) },
                enabled = motivo.isNotBlank()
            ) {
                Text("Confirmar", color = UniforError, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        },
        containerColor = UniforSurface
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Header
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AdminPainelHeader() {
    Column(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)) {
        Text(
            "Painel de\nEmpréstimos",
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            color = UniforPrimary,
            lineHeight = 42.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Gestão centralizada de pendências e fluxos acadêmicos.",
            fontSize = 16.sp,
            color = SubtitleColor,
            lineHeight = 24.sp
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Filter Tabs — duas linhas replicando o layout React exato
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AdminFilterTabs(selectedTab: AdminLoanTab, onTabSelected: (AdminLoanTab) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AdminTabRow(
            tabs = listOf(AdminLoanTab.SOLICITACOES, AdminLoanTab.RETIRADA, AdminLoanTab.APROVADO),
            selectedTab = selectedTab,
            onTabSelected = onTabSelected
        )
        AdminTabRow(
            tabs = listOf(AdminLoanTab.FILA, AdminLoanTab.NEGADO, AdminLoanTab.EM_ATRASO),
            selectedTab = selectedTab,
            onTabSelected = onTabSelected
        )
    }
}

@Composable
private fun AdminTabRow(
    tabs: List<AdminLoanTab>,
    selectedTab: AdminLoanTab,
    onTabSelected: (AdminLoanTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(UniforSurfaceContainerLow)
            .padding(4.dp)
    ) {
        tabs.forEach { tab ->
            val isSelected = tab == selectedTab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (isSelected) Modifier
                            .shadow(1.dp, RoundedCornerShape(10.dp))
                            .background(UniforSurface, RoundedCornerShape(10.dp))
                        else Modifier
                    )
                    .clickable { onTabSelected(tab) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    tab.label,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Bold,
                    color = if (isSelected) UniforPrimary else SubtitleColor
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Summary Card — label e valor variam por aba
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AdminSummaryCard(count: Int, tab: AdminLoanTab, totalMulta: Double) {
    val label = when (tab) {
        AdminLoanTab.EM_ATRASO -> "TOTAL PENDENTE"
        AdminLoanTab.FILA      -> "TOTAL NA FILA DE ESPERA"
        else                   -> "TOTAL DE SOLICITAÇÕES"
    }
    val value = when (tab) {
        AdminLoanTab.EM_ATRASO -> "R$ ${String.format("%.2f", totalMulta).replace('.', ',')}"
        AdminLoanTab.FILA      -> "$count filas de empréstimos"
        else                   -> "$count exemplares"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = UniforSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(label, fontSize = 12.sp, color = SubtitleColor, letterSpacing = 1.2.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = UniforPrimary)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Empty States
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AdminEmptyState(tab: AdminLoanTab) {
    val msg = when (tab) {
        AdminLoanTab.FILA -> "Endpoint de fila admin em desenvolvimento.\n(GET /fila — admin)"
        else              -> "Nenhum registro encontrado para \"${tab.label}\"."
    }
    Box(
        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(msg, color = UniforOutline, fontSize = 14.sp)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Loan Card — adapta conteúdo e botões conforme a aba selecionada
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AdminLoanCard(
    emprestimo: AdminEmprestimo,
    tab: AdminLoanTab,
    isLoading: Boolean,
    onAprovar: () -> Unit,
    onCancelar: () -> Unit,   // abre dialog de negação
    onEntregar: () -> Unit,   // Confirmar Retirada
    onDevolver: () -> Unit,   // Confirmar Devolução
    onQuitarMulta: () -> Unit // Confirmar Pagamento
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = UniforSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Linha superior: capa | status + hora
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                BookCoverThumbnail(emprestimo.livro?.capaUrl)
                LoanStatusBadge(tab = tab, status = emprestimo.status, dataSolicitacao = emprestimo.dataSolicitacao)
            }

            // Título e autor
            Column {
                Text(
                    emprestimo.livro?.titulo ?: "Título não disponível",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = UniforOnSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(emprestimo.livro?.autor ?: "", fontSize = 14.sp, color = SubtitleColor)
            }

            // Datas (Aprovado, Em Atraso, Negado)
            if (tab in setOf(AdminLoanTab.APROVADO, AdminLoanTab.EM_ATRASO, AdminLoanTab.NEGADO)) {
                LoanDatesRow(
                    dataAprovacao = emprestimo.dataAprovacao,
                    dataDevolucaoPrevista = emprestimo.dataDevolucaoPrevista
                )
            }

            // Valor da multa (apenas Em Atraso)
            if (tab == AdminLoanTab.EM_ATRASO) {
                MultaRow(multa = emprestimo.multa)
            }

            // Motivo da negação (apenas Negado)
            if (tab == AdminLoanTab.NEGADO && !emprestimo.motivoNegacao.isNullOrBlank()) {
                MotivoRow(motivo = emprestimo.motivoNegacao)
            }

            // Info do aluno
            StudentInfoRow(emprestimo.usuario)

            // Ações
            if (isLoading) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(Modifier.size(24.dp), color = UniforPrimary, strokeWidth = 2.dp)
                }
            } else {
                AdminCardActions(
                    tab = tab,
                    multa = emprestimo.multa,
                    onAprovar = onAprovar,
                    onCancelar = onCancelar,
                    onEntregar = onEntregar,
                    onDevolver = onDevolver,
                    onQuitarMulta = onQuitarMulta
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Card sub-composables
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun BookCoverThumbnail(capaUrl: String?) {
    Box(
        modifier = Modifier
            .width(48.dp)
            .height(64.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(UniforSurfaceContainerLow),
        contentAlignment = Alignment.Center
    ) {
        if (capaUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(capaUrl).crossfade(true).build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(Icons.Default.MenuBook, null, tint = UniforOutline, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
private fun LoanStatusBadge(tab: AdminLoanTab, status: EmprestimoStatus, dataSolicitacao: String) {
    // Cada aba tem um badge visual próprio, independente do status da API
    val (label, bgColor, textColor) = when (tab) {
        AdminLoanTab.RETIRADA  -> Triple("APROVADO", UniforSuccessContainer, UniforOnSecondaryContainer)
        AdminLoanTab.APROVADO  -> Triple("EM ANDAMENTO", BadgeEmAndamentoBg, BadgeEmAndamentoText)
        AdminLoanTab.EM_ATRASO -> Triple("EM ATRASO", UniforWarningContainer, UniforWarningOnContainer)
        AdminLoanTab.NEGADO    -> Triple("NEGADO", UniforErrorContainer, UniforError)
        AdminLoanTab.FILA      -> Triple("NA FILA", BadgeEmAndamentoBg, BadgeEmAndamentoText)
        else                   -> { // SOLICITACOES e outros — usa o status real
            val (bg, text) = status.badgeColors()
            Triple(status.displayLabel(), bg, text)
        }
    }

    Column(horizontalAlignment = Alignment.End) {
        Surface(color = bgColor, shape = RoundedCornerShape(12.dp)) {
            Text(
                label,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                letterSpacing = 1.sp
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            formatDataSolicitacao(dataSolicitacao),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = SubtitleColor
        )
    }
}

/** Linha com datas de retirada e devolução prevista */
@Composable
private fun LoanDatesRow(dataAprovacao: String?, dataDevolucaoPrevista: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(UniforSurfaceContainerLow)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        DateLine(label = "Retirada", dateIso = dataAprovacao)
        DateLine(label = "Devolução", dateIso = dataDevolucaoPrevista)
    }
}

@Composable
private fun DateLine(label: String, dateIso: String?) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("$label:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = UniforOutline)
        Text(formatDate(dateIso), fontSize = 12.sp, color = SubtitleColor)
    }
}

/** Valor da multa para aba Em Atraso */
@Composable
private fun MultaRow(multa: AdminEmprestimoMulta?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(UniforWarningContainer)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(Icons.Default.Warning, null, tint = UniforWarningOnContainer, modifier = Modifier.size(16.dp))
        Column {
            val valor = multa?.valorTotal?.let { "R$ ${String.format("%.2f", it).replace('.', ',')}" } ?: "R$ -"
            val dias = multa?.diasAtraso ?: 0
            Text("Multa: $valor", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UniforWarningOnContainer)
            if (dias > 0) Text("$dias dia(s) de atraso", fontSize = 11.sp, color = UniforWarningOnContainer)
        }
    }
}

/** Motivo da negação para aba Negado */
@Composable
private fun MotivoRow(motivo: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(UniforErrorContainer)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text("Motivo Cancelamento:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = UniforError)
        Text(motivo, fontSize = 13.sp, color = UniforError)
    }
}

@Composable
private fun StudentInfoRow(usuario: AdminEmprestimoUsuario?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(UniforSurfaceContainerLow)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(UniforPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                usuario?.nome?.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                color = UniforPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
        Column {
            Text(
                usuario?.nome ?: "Aluno não identificado",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = UniforOnSurface
            )
            Text(
                "Matrícula: ${usuario?.matricula ?: "-"}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = SubtitleColor
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Card Actions — botões adaptados por aba
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AdminCardActions(
    tab: AdminLoanTab,
    multa: AdminEmprestimoMulta?,
    onAprovar: () -> Unit,
    onCancelar: () -> Unit,
    onEntregar: () -> Unit,
    onDevolver: () -> Unit,
    onQuitarMulta: () -> Unit
) {
    when (tab) {
        AdminLoanTab.SOLICITACOES -> {
            // Cancelar (→ dialog negar com motivo) + Aprovar
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onCancelar,
                    modifier = Modifier.weight(1f).height(44.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = UniforError),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("Cancelar", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
                Button(
                    onClick = onAprovar,
                    modifier = Modifier.weight(1f).height(44.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = UniforSuccess),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Icon(Icons.Default.Check, null, Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Aprovar", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }
        }
        AdminLoanTab.RETIRADA -> {
            // Cancelar + Confirmar Retirada (→ PATCH /entregar)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onCancelar,
                    modifier = Modifier.weight(1f).height(44.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = UniforError),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("Cancelar", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
                Button(
                    onClick = onEntregar,
                    modifier = Modifier.weight(1f).height(44.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = UniforSuccess),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("Confirmar Retirada", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
            }
        }
        AdminLoanTab.APROVADO -> {
            // Confirmar Devolução (→ PATCH /devolver)
            Button(
                onClick = onDevolver,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UniforPrimary),
                shape = RoundedCornerShape(6.dp)
            ) {
                Icon(Icons.Default.HourglassTop, null, Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Confirmar Devolução", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
        AdminLoanTab.EM_ATRASO -> {
            // Confirmar Pagamento da multa (→ PATCH /multas/{id}/quitar)
            val valor = multa?.valorTotal?.let { " (R$ ${String.format("%.2f", it).replace('.', ',')})" } ?: ""
            Button(
                onClick = onQuitarMulta,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                enabled = multa != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = UniforSuccess,
                    disabledContainerColor = UniforSurfaceContainerHigh
                ),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    "Confirmar Pagamento$valor",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }
        // NEGADO e FILA: somente visualização
        AdminLoanTab.NEGADO, AdminLoanTab.FILA -> Unit
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Helpers
// ─────────────────────────────────────────────────────────────────────────────

private fun EmprestimoStatus.badgeColors(): Pair<Color, Color> = when (this) {
    EmprestimoStatus.PENDENTE  -> BadgePendenteBg to BadgePendenteText
    EmprestimoStatus.APROVADO  -> UniforSuccessContainer to UniforOnSecondaryContainer
    EmprestimoStatus.NEGADO    -> UniforErrorContainer to UniforError
    EmprestimoStatus.ATRASADO  -> UniforWarningContainer to UniforWarningOnContainer
    EmprestimoStatus.DEVOLVIDO -> UniforSurfaceContainerHigh to UniforOutline
    EmprestimoStatus.CANCELADO -> UniforSurfaceContainerHigh to UniforOutline
}

private fun EmprestimoStatus.displayLabel(): String = when (this) {
    EmprestimoStatus.PENDENTE  -> "PENDENTE"
    EmprestimoStatus.APROVADO  -> "APROVADO"
    EmprestimoStatus.NEGADO    -> "NEGADO"
    EmprestimoStatus.ATRASADO  -> "EM ATRASO"
    EmprestimoStatus.DEVOLVIDO -> "DEVOLVIDO"
    EmprestimoStatus.CANCELADO -> "CANCELADO"
}

/** Formata ISO datetime como "Hoje, HH:mm" ou "dd/MM, HH:mm" */
private fun formatDataSolicitacao(iso: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val date = parser.parse(iso) ?: return iso
        val now = Calendar.getInstance()
        val cal = Calendar.getInstance().also { it.time = date }
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
        if (now.get(Calendar.DATE) == cal.get(Calendar.DATE) &&
            now.get(Calendar.MONTH) == cal.get(Calendar.MONTH) &&
            now.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
        ) "Hoje, $time"
        else "${SimpleDateFormat("dd/MM", Locale.getDefault()).format(date)}, $time"
    } catch (e: Exception) { iso }
}

/** Formata ISO datetime como "dd/MM/yyyy" */
private fun formatDate(iso: String?): String {
    iso ?: return "-"
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(parser.parse(iso) ?: return iso)
    } catch (e: Exception) { iso }
}


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.unifor.booksapp.data.models.EmprestimoStatus
import com.unifor.booksapp.data.remote.response.AdminEmprestimo
import com.unifor.booksapp.ui.theme.*
import com.unifor.booksapp.ui.viewmodels.*
import java.text.SimpleDateFormat
import java.util.*

// Cores locais extraídas do protótipo React
private val PanelSubtitleColor = Color(0xFF424751)
private val BadgePendenteBackground = Color(0x4D939393)
private val BadgePendenteText = Color(0xFFA0A0A0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminLoansPanelScreen(
    onBack: () -> Unit,
    viewModel: AdminLoansViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val filteredList by viewModel.filteredEmprestimos.collectAsState()
    val actionState by viewModel.actionState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // ID do empréstimo aguardando motivo de negação; null = dialog fechado
    var negacaoDialogId by remember { mutableStateOf<String?>(null) }
    var negacaoMotivo by remember { mutableStateOf("") }

    // Recarrega a lista ao retornar ao foreground
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.fetchEmprestimos()
        }
    }

    // Feedback das ações de aprovação/negação via Snackbar
    LaunchedEffect(actionState) {
        when (actionState) {
            is AdminActionState.Success -> {
                snackbarHostState.showSnackbar((actionState as AdminActionState.Success).message)
                viewModel.resetActionState()
            }
            is AdminActionState.Error -> {
                snackbarHostState.showSnackbar((actionState as AdminActionState.Error).message)
                viewModel.resetActionState()
            }
            else -> Unit
        }
    }

    // Dialog para capturar o motivo antes de chamar PATCH /negar
    negacaoDialogId?.let { id ->
        AlertDialog(
            onDismissRequest = { negacaoDialogId = null; negacaoMotivo = "" },
            title = { Text("Motivo da Negação", fontWeight = FontWeight.Bold, color = UniforPrimary) },
            text = {
                OutlinedTextField(
                    value = negacaoMotivo,
                    onValueChange = { negacaoMotivo = it },
                    placeholder = { Text("Descreva o motivo...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (negacaoMotivo.isNotBlank()) {
                            viewModel.negar(id, negacaoMotivo)
                            negacaoDialogId = null
                            negacaoMotivo = ""
                        }
                    }
                ) {
                    Text("Confirmar", color = UniforError, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { negacaoDialogId = null; negacaoMotivo = "" }) {
                    Text("Cancelar")
                }
            },
            containerColor = UniforSurface
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Unifor Books Admin",
                        fontWeight = FontWeight.ExtraBold,
                        color = UniforPrimary,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = UniforPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = UniforSurface)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = UniforBackground
    ) { padding ->
        when (val state = uiState) {
            is AdminLoansUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = UniforPrimary)
                }
            }
            is AdminLoansUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(state.message, color = UniforError, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        TextButton(onClick = viewModel::fetchEmprestimos) {
                            Text("Tentar novamente", color = UniforPrimary)
                        }
                    }
                }
            }
            is AdminLoansUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { AdminPainelHeader() }
                    item {
                        AdminFilterTabs(
                            selectedTab = selectedTab,
                            onTabSelected = viewModel::selectTab
                        )
                    }
                    item {
                        AdminSummaryCard(
                            count = filteredList.size,
                            tab = selectedTab
                        )
                    }
                    if (filteredList.isEmpty()) {
                        item { AdminEmptyState(selectedTab) }
                    } else {
                        items(filteredList, key = { it.id }) { emprestimo ->
                            val isActionLoading = actionState is AdminActionState.Loading &&
                                (actionState as AdminActionState.Loading).emprestimoId == emprestimo.id
                            AdminLoanCard(
                                emprestimo = emprestimo,
                                isLoading = isActionLoading,
                                onAprovar = { viewModel.aprovar(emprestimo.id) },
                                onCancelar = { negacaoDialogId = emprestimo.id }, // "Cancelar" → negar com motivo
                                onEntregar = { viewModel.entregar(emprestimo.id) }
                            )
                        }
                    }
                    item { Spacer(Modifier.height(8.dp)) }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Header
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AdminPainelHeader() {
    Column(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)) {
        Text(
            text = "Painel de\nEmpréstimos",
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            color = UniforPrimary,
            lineHeight = 42.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Gestão centralizada de pendências e fluxos acadêmicos.",
            fontSize = 16.sp,
            color = PanelSubtitleColor,
            lineHeight = 24.sp
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Filter Tabs (duas linhas — replica o layout React exato)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AdminFilterTabs(
    selectedTab: AdminLoanTab,
    onTabSelected: (AdminLoanTab) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AdminFilterTabRow(
            tabs = listOf(AdminLoanTab.SOLICITACOES, AdminLoanTab.RETIRADA, AdminLoanTab.APROVADO),
            selectedTab = selectedTab,
            onTabSelected = onTabSelected
        )
        AdminFilterTabRow(
            tabs = listOf(AdminLoanTab.FILA, AdminLoanTab.NEGADO, AdminLoanTab.EM_ATRASO),
            selectedTab = selectedTab,
            onTabSelected = onTabSelected
        )
    }
}

@Composable
private fun AdminFilterTabRow(
    tabs: List<AdminLoanTab>,
    selectedTab: AdminLoanTab,
    onTabSelected: (AdminLoanTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(UniforSurfaceContainerLow)
            .padding(4.dp)
    ) {
        tabs.forEach { tab ->
            val isSelected = tab == selectedTab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (isSelected) {
                            Modifier
                                .shadow(1.dp, RoundedCornerShape(10.dp))
                                .background(UniforSurface, RoundedCornerShape(10.dp))
                        } else {
                            Modifier
                        }
                    )
                    .clickable { onTabSelected(tab) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tab.label,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Bold,
                    color = if (isSelected) UniforPrimary else PanelSubtitleColor
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Summary Card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AdminSummaryCard(count: Int, tab: AdminLoanTab) {
    val label = when (tab) {
        AdminLoanTab.FILA      -> "TOTAL NA FILA DE ESPERA"
        AdminLoanTab.EM_ATRASO -> "TOTAL EM ATRASO"
        else                   -> "TOTAL DE SOLICITAÇÕES"
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = UniforSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = PanelSubtitleColor,
                letterSpacing = 1.2.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "$count exemplares",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = UniforPrimary
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Empty State
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AdminEmptyState(tab: AdminLoanTab) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        val message = if (tab == AdminLoanTab.FILA)
            "Endpoint de fila admin ainda não disponível."
        else
            "Nenhum registro encontrado para \"${tab.label}\"."

        Text(message, color = UniforOutline, fontSize = 14.sp)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Loan Card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AdminLoanCard(
    emprestimo: AdminEmprestimo,
    isLoading: Boolean,
    onAprovar: () -> Unit,
    onCancelar: () -> Unit,  // abre dialog de negação (PATCH /negar)
    onEntregar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = UniforSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Linha superior: capa do livro | status + hora
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                BookCoverThumbnail(capaUrl = emprestimo.livro?.capaUrl)
                StatusTimeColumn(
                    status = emprestimo.status,
                    dataSolicitacao = emprestimo.dataSolicitacao
                )
            }

            // Título e autor
            Column {
                Text(
                    text = emprestimo.livro?.titulo ?: "Título não disponível",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = UniforOnSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = emprestimo.livro?.autor ?: "",
                    fontSize = 14.sp,
                    color = PanelSubtitleColor
                )
            }

            // Info do aluno
            StudentInfoRow(usuario = emprestimo.usuario)

            // Botões de ação ou loading
            if (isLoading) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = UniforPrimary,
                        strokeWidth = 2.dp
                    )
                }
            } else {
                AdminCardActions(
                    status = emprestimo.status,
                    onAprovar = onAprovar,
                    onCancelar = onCancelar,
                    onEntregar = onEntregar
                )
            }
        }
    }
}

@Composable
private fun BookCoverThumbnail(capaUrl: String?) {
    Box(
        modifier = Modifier
            .width(48.dp)
            .height(64.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(UniforSurfaceContainerLow),
        contentAlignment = Alignment.Center
    ) {
        if (capaUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(capaUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                Icons.Default.MenuBook,
                contentDescription = null,
                tint = UniforOutline,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun StatusTimeColumn(status: EmprestimoStatus, dataSolicitacao: String) {
    Column(horizontalAlignment = Alignment.End) {
        val (bgColor, textColor) = status.badgeColors()
        Surface(color = bgColor, shape = RoundedCornerShape(12.dp)) {
            Text(
                text = status.displayLabel(),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                letterSpacing = 1.sp
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = formatDataSolicitacao(dataSolicitacao),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = PanelSubtitleColor
        )
    }
}

@Composable
private fun StudentInfoRow(usuario: com.unifor.booksapp.data.remote.response.AdminEmprestimoUsuario?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(UniforSurfaceContainerLow)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Avatar com inicial do nome
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(UniforPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = usuario?.nome?.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                color = UniforPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
        Column {
            Text(
                text = usuario?.nome ?: "Aluno não identificado",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = UniforOnSurface
            )
            Text(
                text = "Matrícula: ${usuario?.matricula ?: "-"}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = PanelSubtitleColor
            )
        }
    }
}

@Composable
private fun AdminCardActions(
    status: EmprestimoStatus,
    onAprovar: () -> Unit,
    onCancelar: () -> Unit,
    onEntregar: () -> Unit
) {
    when (status) {
        EmprestimoStatus.PENDENTE -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // "Cancelar" aciona o dialog de negação para colher o motivo (PATCH /negar)
                Button(
                    onClick = onCancelar,
                    modifier = Modifier.weight(1f).height(44.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = UniforError),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("Cancelar", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
                Button(
                    onClick = onAprovar,
                    modifier = Modifier.weight(1f).height(44.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = UniforSuccess),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Aprovar", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }
        }
        // APROVADO sem devolução prevista = esperando retirada física
        EmprestimoStatus.APROVADO -> {
            Button(
                onClick = onEntregar,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UniforSuccess),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("Confirmar Retirada", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
        // Demais estados: somente visualização no painel
        else -> Unit
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Helpers
// ─────────────────────────────────────────────────────────────────────────────

private fun EmprestimoStatus.badgeColors(): Pair<Color, Color> = when (this) {
    EmprestimoStatus.PENDENTE  -> BadgePendenteBackground to BadgePendenteText
    EmprestimoStatus.APROVADO  -> UniforSuccessContainer to UniforOnSecondaryContainer
    EmprestimoStatus.NEGADO    -> UniforErrorContainer to UniforError
    EmprestimoStatus.ATRASADO  -> UniforWarningContainer to UniforWarningOnContainer
    EmprestimoStatus.DEVOLVIDO -> UniforSurfaceContainerHigh to UniforOutline
    EmprestimoStatus.CANCELADO -> UniforSurfaceContainerHigh to UniforOutline
}

private fun EmprestimoStatus.displayLabel(): String = when (this) {
    EmprestimoStatus.PENDENTE  -> "PENDENTE"
    EmprestimoStatus.APROVADO  -> "APROVADO"
    EmprestimoStatus.NEGADO    -> "NEGADO"
    EmprestimoStatus.ATRASADO  -> "EM ATRASO"
    EmprestimoStatus.DEVOLVIDO -> "DEVOLVIDO"
    EmprestimoStatus.CANCELADO -> "CANCELADO"
}

private fun formatDataSolicitacao(iso: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val date = parser.parse(iso) ?: return iso
        val now = Calendar.getInstance()
        val dateCal = Calendar.getInstance().also { it.time = date }
        val timeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
        if (now.get(Calendar.DATE) == dateCal.get(Calendar.DATE) &&
            now.get(Calendar.MONTH) == dateCal.get(Calendar.MONTH) &&
            now.get(Calendar.YEAR) == dateCal.get(Calendar.YEAR)
        ) {
            "Hoje, $timeStr"
        } else {
            "${SimpleDateFormat("dd/MM", Locale.getDefault()).format(date)}, $timeStr"
        }
    } catch (e: Exception) {
        iso
    }
}
