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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unifor.booksapp.ui.theme.*

// Models
enum class LoanStatus(val label: String, val color: Color, val icon: @Composable () -> Unit) {
    ON_TIME("EM DIA", UniforSuccess, { Icon(Icons.Default.CheckCircle, null, tint = UniforSuccess) }),
    LATE("ATRASADO", UniforError, { Icon(Icons.Default.Error, null, tint = UniforError) }),
    IN_QUEUE("NA FILA", UniforWarning, { Icon(Icons.Default.HourglassTop, null, tint = UniforWarning) })
}

data class LoanedBook(
    val id: String,
    val title: String,
    val author: String,
    val dueDate: String,
    val status: LoanStatus,
    val isRenewable: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLoansScreen(
    onNavigateToFines: () -> Unit,
    onNavigateToRenewal: (Boolean) -> Unit,
    onBack: () -> Unit,
    onNavigateToProfile: () -> Unit,
    loans: List<LoanedBook> = emptyList(),
    onTimeCount: Int? = null,
    lateCount: Int? = null,
    queueCount: Int? = null
) {
    Scaffold(
        topBar = { MyLoansTopBar(onBack = onBack, onNavigateToProfile = onNavigateToProfile) },
        containerColor = UniforBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            Text("Meus Empréstimos", fontSize = 36.sp, fontWeight = FontWeight.Black, color = UniforPrimary)
            Text("Gerencie seus livros ativos e acompanhe prazos de devolução.", fontSize = 16.sp, color = UniforOutline, modifier = Modifier.padding(top = 8.dp))
            Spacer(Modifier.height(32.dp))
            GeneralStatusCard(onTimeCount, lateCount, queueCount)
            Spacer(Modifier.height(24.dp))
            FinesCard(onClick = onNavigateToFines)
            Spacer(Modifier.height(32.dp))

            loans.forEach { book ->
                LoanedBookCard(book = book, onRenew = { onNavigateToRenewal(book.isRenewable) })
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyLoansTopBar(onBack: () -> Unit, onNavigateToProfile: () -> Unit) {
    TopAppBar(
        title = { Text("Unifor Books", fontWeight = FontWeight.Black, color = UniforPrimary) },
        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = UniforPrimary) } },
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
private fun GeneralStatusCard(onTime: Int?, late: Int?, queue: Int?) {
    Surface(shape = RoundedCornerShape(20.dp), color = UniforSurface, shadowElevation = 2.dp) {
        Column(Modifier.padding(20.dp)) {
            Text("STATUS GERAL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = UniforOutline)
            Spacer(Modifier.height(8.dp))
            val total = (onTime ?: 0) + (late ?: 0) + (queue ?: 0)
            Text("Você possui $total livros em curso", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = UniforPrimary)
            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                StatusItem(count = onTime, label = "NO PRAZO")
                StatusItem(count = late, label = "ATRASADO", UniforError)
                StatusItem(count = queue, label = "NA FILA", UniforWarning)
            }
        }
    }
}

@Composable
private fun StatusItem(count: Int?, label: String, color: Color = UniforSuccess) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(count?.toString()?.padStart(2, '0') ?: "--", fontSize = 36.sp, fontWeight = FontWeight.Black, color = color)
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = UniforOutline)
    }
}

@Composable
private fun FinesCard(onClick: () -> Unit) {
    Surface(onClick = onClick, shape = RoundedCornerShape(20.dp), color = UniforPrimary) {
        Row(Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Info, null, tint = Color.White)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text("Dúvidas sobre multas?", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Button(onClick = onClick, colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))) {
                Text("Saiba mais", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun LoanedBookCard(book: LoanedBook, onRenew: () -> Unit) {
    Surface(shape = RoundedCornerShape(20.dp), color = UniforSurface, shadowElevation = 1.dp) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                book.status.icon.invoke()
                Spacer(Modifier.width(8.dp))
                Text(book.status.label, color = book.status.color, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(12.dp))
            Row {
                Box(Modifier.size(80.dp, 110.dp).background(UniforSurfaceContainerHigh, RoundedCornerShape(8.dp)))
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(book.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(book.author, color = UniforOutline)
                    Spacer(Modifier.height(8.dp))
                    Text("Devolução em ${book.dueDate}", fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(12.dp))
            Button(onClick = onRenew, enabled = book.isRenewable, modifier = Modifier.fillMaxWidth()) {
                Text("Renovar")
            }
        }
    }
}
