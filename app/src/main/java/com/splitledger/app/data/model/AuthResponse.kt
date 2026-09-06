package com.splitledger.app.data.model

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val email: String,
    val name: String,
    val role: String
)
