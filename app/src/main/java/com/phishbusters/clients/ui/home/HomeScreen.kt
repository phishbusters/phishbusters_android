package com.phishbusters.clients.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.phishbusters.clients.model.ClientStatistics
import com.phishbusters.clients.model.Company
import com.phishbusters.clients.ui.components.AppAccordion
import com.phishbusters.clients.ui.components.AppSnackBarHost
import com.phishbusters.clients.ui.components.AppTopBar
import com.phishbusters.clients.ui.components.BarChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    showTopAppBar: Boolean,
    openDrawer: () -> Unit,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)

    Scaffold(
        snackbarHost = { AppSnackBarHost(hostState = snackBarHostState) },
        topBar = {
            if (showTopAppBar) {
                AppTopBar(
                    openDrawer = openDrawer,
                    topAppBarState = topAppBarState
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        val contentModifier = Modifier
            .padding(innerPadding)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
        Column(
            modifier = contentModifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Inicio",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            uiState.statistics?.let { PhishingStats(it) }
            Spacer(modifier = Modifier.height(16.dp))
            if (uiState.companies.isNotEmpty()) {
                PartnerCompanies(uiState.companies)
            }
        }
    }
}

@Composable
private fun PhishingStats(
    statistics: ClientStatistics
) {
    val values = listOf(
        statistics.phishingAttempts.toFloat(),
        statistics.successfulBlocks.toFloat()
    )

    BarChart(
        values = values,
        legends = listOf("Phishing Attempts", "Successful Blocks"),
        maxHeight = 200.dp
    )
}

@Composable
private fun PartnerCompanies(companies: List<Company>) {
    AppAccordion(
        title = "Empresas que ofrecen acceso gratuito",
        content = {
            companies.forEach { company ->
                CompanyItem(company = company)
            }
        }
    )
}

@Composable
private fun CompanyItem(company: Company) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        AsyncImage(model = company.imageUrl, contentDescription = "Logo de ${company.name}")
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = company.name, style = MaterialTheme.typography.bodyMedium)
    }
}