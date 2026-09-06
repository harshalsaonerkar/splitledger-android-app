package com.splitledger.app.data.api

import com.splitledger.app.data.model.AuthResponse
import com.splitledger.app.data.model.LoginRequest
import com.splitledger.app.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
}