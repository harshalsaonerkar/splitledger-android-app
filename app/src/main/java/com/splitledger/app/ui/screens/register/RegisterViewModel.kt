package com.splitledger.app.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitledger.app.data.api.RetrofitClient
import com.splitledger.app.data.model.RegisterRequest
import com.splitledger.app.data.preferences.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class RegisterViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun register(name: String, email: String, password: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _uiState.value = RegisterUiState(errorMessage = "All fields are required")
            return
        }
        if (password.length < 6) {
            _uiState.value = RegisterUiState(errorMessage = "Password must be at least 6 characters")
            return
        }

        viewModelScope.launch {
            _uiState.value = RegisterUiState(isLoading = true)
            try {
                val response = RetrofitClient.authApi.register(
                    RegisterRequest(name.trim(), email.trim(), password)
                )
                if (response.isSuccessful) {
                    val body = response.body()!!
                    tokenManager.saveAuthData(
                        body.accessToken, body.email, body.name)
                    _uiState.value = RegisterUiState(isSuccess = true)
                } else {
                    _uiState.value = RegisterUiState(
                        errorMessage = "Email already registered")
                }
            } catch (e: Exception) {
                _uiState.value = RegisterUiState(
                    errorMessage = "Cannot connect to server")
            }
        }
    }
}