package com.splitledger.app.data.model

data class BalanceResponse(
    val userId: String,
    val userEmail: String,
    val netBalance: Double,
    val status: String  // OWED, OWES, SETTLED
)

data class SettlementResponse(
    val fromEmail: String,
    val toEmail: String,
    val amount: Double
)
