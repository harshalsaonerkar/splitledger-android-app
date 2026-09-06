package com.splitledger.app.ui.screens.expense

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.splitledger.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    groupId: String,
    viewModel: AddExpenseViewModel,
    onExpenseAdded: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedSplitType by remember { mutableStateOf("EQUAL") }

    LaunchedEffect(groupId) {
        viewModel.loadGroupMembers(groupId)
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onExpenseAdded()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Expense", color = OnBackground) },
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                placeholder = { Text("e.g. Hotel booking, Dinner") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Divider,
                    focusedLabelColor = Primary,
                    cursorColor = Primary,
                    focusedTextColor = OnSurface,
                    unfocusedTextColor = OnSurface
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Amount
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount (₹)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Divider,
                    focusedLabelColor = Primary,
                    cursorColor = Primary,
                    focusedTextColor = OnSurface,
                    unfocusedTextColor = OnSurface
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Split type selector
            Text(
                text = "SPLIT TYPE",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("EQUAL", "EXACT", "PERCENTAGE").forEach { type ->
                    FilterChip(
                        selected = selectedSplitType == type,
                        onClick = { selectedSplitType = type },
                        label = { Text(type) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Primary,
                            selectedLabelColor = OnPrimary,
                            containerColor = SurfaceVariant,
                            labelColor = TextSecondary
                        )
                    )
                }
            }

            // Members included in split
            if (uiState.members.isNotEmpty()) {
                Text(
                    text = "SPLIT BETWEEN",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                uiState.members.forEach { member ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Surface),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = member.userEmail,
                                color = OnBackground,
                                modifier = Modifier.weight(1f),
                                fontSize = 14.sp
                            )
                            if (selectedSplitType == "EQUAL") {
                                val splitAmount = amount.toDoubleOrNull()
                                    ?.div(uiState.members.size)
                                Text(
                                    text = splitAmount?.let {
                                        "₹${"%.2f".format(it)}" } ?: "-",
                                    color = Primary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            uiState.errorMessage?.let {
                Text(text = it, color = Error, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.createExpense(
                        groupId = groupId,
                        description = description,
                        amount = amount.toDoubleOrNull() ?: 0.0,
                        splitType = selectedSplitType
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !uiState.isLoading &&
                        description.isNotBlank() &&
                        amount.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = OnPrimary,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Add Expense", fontSize = 16.sp)
                }
            }
        }
    }
}