package com.splitledger.app.ui.screens.balance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.splitledger.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalancesScreen(
    groupId: String,
    viewModel: BalancesViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(groupId) {
        viewModel.loadBalances(groupId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Balances", color = OnBackground) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = OnBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface)
            )
        },
        containerColor = Background
    ) { padding ->

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                item {
                    Text(
                        text = "NET BALANCES",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(uiState.balances) { balance ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Surface),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = balance.userEmail,
                                    color = OnBackground,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = balance.status,
                                    color = when (balance.status) {
                                        "OWED" -> Success
                                        "OWES" -> Error
                                        else -> TextSecondary
                                    },
                                    fontSize = 12.sp
                                )
                            }
                            Text(
                                text = "₹${"%.2f".format(
                                    Math.abs(balance.netBalance))}",
                                color = when (balance.status) {
                                    "OWED" -> Success
                                    "OWES" -> Error
                                    else -> TextSecondary
                                },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (uiState.settlements.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "MINIMUM SETTLEMENTS",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${uiState.settlements.size} transaction(s) to settle all debts",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    items(uiState.settlements) { settlement ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = SurfaceVariant),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = settlement.fromEmail,
                                        color = Error,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "  pays  →",
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = settlement.toEmail,
                                        color = Success,
                                        fontSize = 13.sp
                                    )
                                }
                                Text(
                                    text = "₹${"%.2f".format(
                                        settlement.amount)}",
                                    color = Primary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}