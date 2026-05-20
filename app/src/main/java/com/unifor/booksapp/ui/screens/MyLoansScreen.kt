package com.unifor.booksapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unifor.booksapp.data.models.Emprestimo
import com.unifor.booksapp.data.models.EmprestimoStatus
import com.unifor.booksapp.data.models.FilaEspera
import com.unifor.booksapp.ui.theme.*
import com.unifor.booksapp.ui.viewmodels.MyLoansUiState
import com.unifor.booksapp.ui.viewmodels.MyLoansViewModel
import com.unifor.booksapp.ui.viewmodels.RenovacaoUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLoansScreen(
    onNavigateToFines: () -> Unit,
    onNavigateToRenewal: (Boolean) -> Unit,
    onBack: () -> Unit,
    onNavigateToProfile: () -> Unit,
    myLoansViewModel: MyLoansViewModel = viewModel()
) {
    val uiState by myLoansViewModel.uiState.collectAsState()
    val renovacaoState by myLoansViewModel.renovacaoState.collectAsState()

    // Recarrega sempre que a tela volta ao foreground (ex: após solicitar um empréstimo)
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            myLoansViewModel.fetchLoans()
        }
    }

    // Reage ao resultado da renovação
    LaunchedEffect(renovacaoState) {
        when (renovacaoState) {
            is RenovacaoUiState.Sucesso -> {
                myLoansViewModel.resetRenovacaoState()
                onNavigateToRenewal(true)
            }
            is RenovacaoUiState.Indisponivel -> {
                myLoansViewModel.resetRenovacaoState()
                onNavigateToRenewal(false)
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = { MyLoansTopBar(onBack = onBack, onNavigateToProfile = onNavigateToProfile) },
        containerColor = UniforBackground
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                is MyLoansUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is MyLoansUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Erro ao carregar empréstimos",
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = myLoansViewModel::fetchLoans) {
                            Text("Tentar novamente", color = UniforPrimary)
                        }
                    }
                }
                is MyLoansUiState.Success -> {
                    MyLoansContent(
                        emprestimos = state.data.emprestimos,
                        filaEspera = state.data.filaEspera,
                        renovacaoLoading = renovacaoState is RenovacaoUiState.Loading,
                        onNavigateToFines = onNavigateToFines,
                        onRenovar = { myLoansViewModel.renovarEmprestimo(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MyLoansContent(
    emprestimos: List<Emprestimo>,
    filaEspera: List<FilaEspera>,
    renovacaoLoading: Boolean,
    onNavigateToFines: () -> Unit,
    onRenovar: (String) -> Unit
) {
    val ativos = emprestimos.filter {
        it.status in listOf(
            EmprestimoStatus.APROVADO,
            EmprestimoStatus.ATRASADO,
            EmprestimoStatus.PENDENTE
        )
    }
    val onTimeCount = emprestimos.count { it.status == EmprestimoStatus.APROVADO }
    val lateCount = emprestimos.count { it.status == EmprestimoStatus.ATRASADO }
    val pendingCount = emprestimos.count { it.status == EmprestimoStatus.PENDENTE }
    val queueCount = filaEspera.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        Text("Meus Empréstimos", fontSize = 36.sp, fontWeight = FontWeight.Black, color = UniforPrimary)
        Text(
            "Gerencie seus livros ativos e acompanhe prazos de devolução.",
            fontSize = 16.sp,
            color = UniforOutline,
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(Modifier.height(32.dp))
        GeneralStatusCard(
            onTime = onTimeCount,
            pending = pendingCount,
            late = lateCount,
            queue = queueCount
        )
        Spacer(Modifier.height(24.dp))
        FinesCard(onClick = onNavigateToFines)
        Spacer(Modifier.height(32.dp))

        if (ativos.isEmpty() && filaEspera.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhum empréstimo ativo no momento.", color = UniforOutline)
            }
        }

        ativos.forEach { emprestimo ->
            EmprestimoCard(
                emprestimo = emprestimo,
                renovacaoLoading = renovacaoLoading,
                onRenovar = { onRenovar(emprestimo.id) }
            )
            Spacer(Modifier.height(16.dp))
        }

        if (filaEspera.isNotEmpty()) {
            Text("Na Fila de Espera", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = UniforPrimary)
            Spacer(Modifier.height(12.dp))
            filaEspera.forEach { fila ->
                FilaEsperaCard(fila = fila)
                Spacer(Modifier.height(12.dp))
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyLoansTopBar(onBack: () -> Unit, onNavigateToProfile: () -> Unit) {
    TopAppBar(
        title = { Text("Unifor Books", fontWeight = FontWeight.Black, color = UniforPrimary) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = UniforPrimary)
            }
        },
        actions = {
            Box(
                Modifier
                    .padding(end = 16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(UniforSurfaceContainerHigh)
                    .clickable { onNavigateToProfile() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, tint = UniforPrimary)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = UniforBackground)
    )
}

@Composable
private fun GeneralStatusCard(onTime: Int, pending: Int, late: Int, queue: Int) {
    Surface(shape = RoundedCornerShape(20.dp), color = UniforSurface, shadowElevation = 2.dp) {
        Column(Modifier.padding(20.dp)) {
            Text("STATUS GERAL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = UniforOutline)
            Spacer(Modifier.height(8.dp))
            val total = onTime + pending + late + queue
            Text(
                "Você possui $total livros em curso",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = UniforPrimary
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatusItem(count = onTime, label = "NO PRAZO", color = UniforSuccess)
                StatusItem(count = pending, label = "PENDENTE", color = UniforWarning)
                StatusItem(count = late, label = "ATRASADO", color = UniforError)
                StatusItem(count = queue, label = "NA FILA", color = UniforOutline)
            }
        }
    }
}

@Composable
private fun StatusItem(count: Int, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            count.toString().padStart(2, '0'),
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = color
        )
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = UniforOutline)
    }
}

@Composable
private fun FinesCard(onClick: () -> Unit) {
    Surface(onClick = onClick, shape = RoundedCornerShape(20.dp), color = UniforPrimary) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Info, null, tint = Color.White)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text("Dúvidas sobre multas?", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
            ) {
                Text("Saiba mais", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun EmprestimoCard(
    emprestimo: Emprestimo,
    renovacaoLoading: Boolean,
    onRenovar: () -> Unit
) {
    val (statusLabel, statusColor, statusIcon) = when (emprestimo.status) {
        EmprestimoStatus.APROVADO -> Triple("EM DIA", UniforSuccess, Icons.Default.CheckCircle)
        EmprestimoStatus.ATRASADO -> Triple("ATRASADO", UniforError, Icons.Default.Error)
        EmprestimoStatus.PENDENTE -> Triple("PENDENTE", UniforWarning, Icons.Default.HourglassTop)
        else -> Triple(emprestimo.status.name, UniforOutline, Icons.Default.Info)
    }

    val isRenewable = emprestimo.status == EmprestimoStatus.APROVADO ||
            emprestimo.status == EmprestimoStatus.ATRASADO

    Surface(shape = RoundedCornerShape(20.dp), color = UniforSurface, shadowElevation = 1.dp) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(statusIcon, null, tint = statusColor)
                Spacer(Modifier.width(8.dp))
                Text(statusLabel, color = statusColor, fontWeight = FontWeight.Bold)
                if (emprestimo.status == EmprestimoStatus.PENDENTE) {
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Aguardando aprovação",
                        fontSize = 12.sp,
                        color = UniforOutline
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Row {
                Box(
                    Modifier
                        .size(80.dp, 110.dp)
                        .background(UniforSurfaceContainerHigh, RoundedCornerShape(8.dp))
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        "Exemplar #${emprestimo.exemplarId.takeLast(6)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        "Solicitado em ${emprestimo.dataSolicitacao.take(10)}",
                        color = UniforOutline,
                        fontSize = 13.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    if (emprestimo.dataDevolucaoPrevista != null) {
                        Text(
                            "Devolução: ${emprestimo.dataDevolucaoPrevista.take(10)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onRenovar,
                enabled = isRenewable && !renovacaoLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = UniforPrimary)
            ) {
                if (renovacaoLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Renovar Empréstimo")
                }
            }
        }
    }
}

@Composable
private fun FilaEsperaCard(fila: FilaEspera) {
    Surface(shape = RoundedCornerShape(16.dp), color = UniforSurface, shadowElevation = 1.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(UniforWarning.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "#${fila.posicao}",
                    fontWeight = FontWeight.Black,
                    color = UniforWarning,
                    fontSize = 14.sp
                )
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text("Livro ID: ${fila.livroId.takeLast(8)}", fontWeight = FontWeight.Bold)
                Text("Posição na fila: ${fila.posicao}", color = UniforOutline, fontSize = 13.sp)
                Text("Status: ${fila.status}", color = UniforOutline, fontSize = 12.sp)
            }
        }
    }
}
