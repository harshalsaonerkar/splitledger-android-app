package com.splitledger.app.ui.screens.group

import androidx.compose.foundation.background
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
fun GroupDetailScreen(
    groupId: String,
    viewModel: GroupDetailViewModel,
    onAddExpense: (String) -> Unit,
    onViewBalances: (String) -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddMember by remember { mutableStateOf(false) }
    var memberEmail by remember { mutableStateOf("") }

    LaunchedEffect(groupId) {
        viewModel.loadGroup(groupId)
        viewModel.loadExpenses(groupId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.group?.name ?: "Group",
                        color = OnBackground
                    )
                },
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Action buttons
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { onAddExpense(groupId) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("+ Add Expense")
                    }
                    OutlinedButton(
                        onClick = { onViewBalances(groupId) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Primary)
                    ) {
                        Text("Balances")
                    }
                }
            }

            // Members section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MEMBERS",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { showAddMember = true }) {
                        Text("+ Add", color = Primary, fontSize = 13.sp)
                    }
                }
            }

            // Members list
            uiState.group?.members?.let { members ->
                items(members) { member ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Surface),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(
                                        SurfaceVariant,
                                        RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = member.userEmail
                                        .first().uppercaseChar().toString(),
                                    color = Primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = member.userEmail,
                                    color = OnBackground,
                                    fontSize = 14.sp
                                )
                            }
                            Surface(
                                color = if (member.role == "ADMIN")
                                    Primary.copy(alpha = 0.2f)
                                else SurfaceVariant,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = member.role,
                                    color = if (member.role == "ADMIN")
                                        Primary else TextSecondary,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(
                                        horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Expenses section
            item {
                Text(
                    text = "EXPENSES",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (uiState.expenses.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No expenses yet",
                            color = TextSecondary
                        )
                    }
                }
            } else {
                items(uiState.expenses) { expense ->
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
                                    text = expense.description,
                                    color = OnBackground,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Paid by ${expense.paidByEmail}",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                            Text(
                                text = "₹${"%.2f".format(expense.amount)}",
                                color = Primary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        // Add member dialog
        if (showAddMember) {
            AlertDialog(
                onDismissRequest = { showAddMember = false },
                containerColor = Surface,
                title = { Text("Add Member", color = OnBackground) },
                text = {
                    OutlinedTextField(
                        value = memberEmail,
                        onValueChange = { memberEmail = it },
                        label = { Text("Member Email") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            unfocusedBorderColor = Divider,
                            focusedLabelColor = Primary,
                            cursorColor = Primary,
                            focusedTextColor = OnSurface,
                            unfocusedTextColor = OnSurface
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.addMember(groupId, memberEmail)
                        showAddMember = false
                        memberEmail = ""
                    }) {
                        Text("Add", color = Primary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddMember = false }) {
                        Text("Cancel", color = TextSecondary)
                    }
                }
            )
        }
    }
}