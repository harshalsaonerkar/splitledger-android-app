package com.splitledger.app.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitledger.app.data.api.RetrofitClient
import com.splitledger.app.data.model.LoginRequest
import com.splitledger.app.data.preferences.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class LoginViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState(errorMessage = "Email and password are required")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)
            try {
                val response = RetrofitClient.authApi.login(
                    LoginRequest(email.trim(), password)
                )
                if (response.isSuccessful) {
                    val body = response.body()!!
                    tokenManager.saveAuthData(
                        body.accessToken, body.email, body.name)
                    _uiState.value = LoginUiState(isSuccess = true)
                } else {
                    _uiState.value = LoginUiState(
                        errorMessage = "Invalid email or password")
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState(
                    errorMessage = "Cannot connect to server")
            }
        }
    }
}