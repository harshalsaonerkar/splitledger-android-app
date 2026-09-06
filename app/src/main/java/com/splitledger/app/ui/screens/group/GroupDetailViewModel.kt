package com.splitledger.app.ui.screens.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitledger.app.data.api.RetrofitClient
import com.splitledger.app.data.model.AddMemberRequest
import com.splitledger.app.data.model.ExpenseResponse
import com.splitledger.app.data.model.GroupResponse
import com.splitledger.app.data.preferences.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class GroupDetailUiState(
    val isLoading: Boolean = false,
    val group: GroupResponse? = null,
    val expenses: List<ExpenseResponse> = emptyList(),
    val errorMessage: String? = null
)

class GroupDetailViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupDetailUiState())
    val uiState: StateFlow<GroupDetailUiState> = _uiState

    fun loadGroup(groupId: String) {
        viewModelScope.launch {
            try {
                val token = "Bearer ${tokenManager.accessToken.first()}"
                val response = RetrofitClient.groupApi.getGroup(token, groupId)
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        group = response.body())
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to load group")
            }
        }
    }

    fun loadExpenses(groupId: String) {
        viewModelScope.launch {
            try {
                val token = "Bearer ${tokenManager.accessToken.first()}"
                val response = RetrofitClient.expenseApi
                    .getGroupExpenses(token, groupId)
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        expenses = response.body() ?: emptyList())
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to load expenses")
            }
        }
    }

    fun addMember(groupId: String, email: String) {
        if (email.isBlank()) return
        viewModelScope.launch {
            try {
                val token = "Bearer ${tokenManager.accessToken.first()}"
                val response = RetrofitClient.groupApi.addMember(
                    token, groupId, AddMemberRequest(email.trim()))
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        group = response.body())
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to add member")
            }
        }
    }
}