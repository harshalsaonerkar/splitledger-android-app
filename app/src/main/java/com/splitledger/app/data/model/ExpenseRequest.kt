package com.splitledger.app.data.model

data class CreateExpenseRequest(
    val groupId: String,
    val description: String,
    val amount: Double,
    val splitType: String,
    val splits: List<SplitRequest>
)

data class SplitRequest(
    val userEmail: String,
    val amount: Double? = null,
    val percentage: Double? = null
)
