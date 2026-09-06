package com.splitledger.app.data.model

data class ExpenseResponse(
    val id: String,
    val groupId: String,
    val paidBy: String,
    val paidByEmail: String,
    val description: String,
    val amount: Double,
    val splitType: String,
    val settled: Boolean,
    val splits: List<SplitDto>
)

data class SplitDto(
    val userId: String,
    val userEmail: String,
    val amount: Double,
    val paid: Boolean
)
