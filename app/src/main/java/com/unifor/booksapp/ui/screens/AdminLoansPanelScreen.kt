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
