package com.splitledger.app.ui.screens.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitledger.app.data.api.RetrofitClient
import com.splitledger.app.data.model.BalanceResponse
import com.splitledger.app.data.model.SettlementResponse
import com.splitledger.app.data.preferences.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class BalancesUiState(
    val isLoading: Boolean = false,
    val balances: List<BalanceResponse> = emptyList(),
    val settlements: List<SettlementResponse> = emptyList(),
    val errorMessage: String? = null
)

class BalancesViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val _uiState = MutableStateFlow(BalancesUiState())
    val uiState: StateFlow<BalancesUiState> = _uiState

    fun loadBalances(groupId: String) {
        viewModelScope.launch {
            _uiState.value = BalancesUiState(isLoading = true)
            try {
                val token = "Bearer ${tokenManager.accessToken.first()}"

                val balancesResponse = RetrofitClient.ledgerApi
                    .getBalances(token, groupId)
                val settlementsResponse = RetrofitClient.ledgerApi
                    .getSettlements(token, groupId)

                _uiState.value = BalancesUiState(
                    balances = balancesResponse.body() ?: emptyList(),
                    settlements = settlementsResponse.body() ?: emptyList()
                )
            } catch (e: Exception) {
                _uiState.value = BalancesUiState(
                    errorMessage = "Failed to load balances")
            }
        }
    }
}