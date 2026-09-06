package com.splitledger.app.ui.screens.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitledger.app.data.api.RetrofitClient
import com.splitledger.app.data.model.CreateExpenseRequest
import com.splitledger.app.data.model.MemberDto
import com.splitledger.app.data.model.SplitRequest
import com.splitledger.app.data.preferences.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class AddExpenseUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val members: List<MemberDto> = emptyList(),
    val errorMessage: String? = null
)

class AddExpenseViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val _uiState = MutableStateFlow(AddExpenseUiState())
    val uiState: StateFlow<AddExpenseUiState> = _uiState

    fun loadGroupMembers(groupId: String) {
        viewModelScope.launch {
            try {
                val token = "Bearer ${tokenManager.accessToken.first()}"
                val response = RetrofitClient.groupApi.getGroup(token, groupId)
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        members = response.body()?.members ?: emptyList())
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to load members")
            }
        }
    }

    fun createExpense(
        groupId: String,
        description: String,
        amount: Double,
        splitType: String
    ) {
        if (description.isBlank() || amount <= 0) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val token = "Bearer ${tokenManager.accessToken.first()}"
                val members = _uiState.value.members

                val splits = members.map { member ->
                    SplitRequest(userEmail = member.userEmail)
                }

                val response = RetrofitClient.expenseApi.createExpense(
                    token,
                    CreateExpenseRequest(
                        groupId = groupId,
                        description = description,
                        amount = amount,
                        splitType = splitType,
                        splits = splits
                    )
                )

                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true)
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to add expense")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Cannot connect to server")
            }
        }
    }
}