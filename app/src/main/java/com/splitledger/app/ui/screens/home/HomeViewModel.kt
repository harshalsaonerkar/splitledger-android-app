package com.splitledger.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitledger.app.data.api.RetrofitClient
import com.splitledger.app.data.model.GroupResponse
import com.splitledger.app.data.preferences.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val groups: List<GroupResponse> = emptyList(),
    val errorMessage: String? = null
)

class HomeViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    fun loadGroups() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(isLoading = true)
            try {
                val token = "Bearer ${tokenManager.accessToken.first()}"
                val response = RetrofitClient.groupApi.getMyGroups(token)
                if (response.isSuccessful) {
                    _uiState.value = HomeUiState(
                        groups = response.body() ?: emptyList())
                } else {
                    _uiState.value = HomeUiState(
                        errorMessage = "Failed to load groups")
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState(
                    errorMessage = "Cannot connect to server")
            }
        }
    }
}