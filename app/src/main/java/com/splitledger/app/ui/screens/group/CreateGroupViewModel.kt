package com.splitledger.app.ui.screens.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitledger.app.data.api.RetrofitClient
import com.splitledger.app.data.model.CreateGroupRequest
import com.splitledger.app.data.preferences.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class CreateGroupUiState(
    val isLoading: Boolean = false,
    val createdGroupId: String? = null,
    val errorMessage: String? = null
)

class CreateGroupViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateGroupUiState())
    val uiState: StateFlow<CreateGroupUiState> = _uiState

    fun createGroup(name: String, description: String) {
        if (name.isBlank()) return

        viewModelScope.launch {
            _uiState.value = CreateGroupUiState(isLoading = true)
            try {
                val token = "Bearer ${tokenManager.accessToken.first()}"
                val response = RetrofitClient.groupApi.createGroup(
                    token,
                    CreateGroupRequest(name.trim(), description.trim())
                )
                if (response.isSuccessful) {
                    _uiState.value = CreateGroupUiState(
                        createdGroupId = response.body()?.id)
                } else {
                    _uiState.value = CreateGroupUiState(
                        errorMessage = "Failed to create group")
                }
            } catch (e: Exception) {
                _uiState.value = CreateGroupUiState(
                    errorMessage = "Cannot connect to server")
            }
        }
    }
}