package com.splitledger.app.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val AUTH_BASE_URL    = "http://10.0.2.2:8081/"
    private const val GROUP_BASE_URL   = "http://10.0.2.2:8082/"
    private const val EXPENSE_BASE_URL = "http://10.0.2.2:8083/"
    private const val LEDGER_BASE_URL  = "http://10.0.2.2:8084/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private fun buildRetrofit(baseUrl: String) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApi: AuthApi =
        buildRetrofit(AUTH_BASE_URL).create(AuthApi::class.java)

    val groupApi: GroupApi =
        buildRetrofit(GROUP_BASE_URL).create(GroupApi::class.java)

    val expenseApi: ExpenseApi =
        buildRetrofit(EXPENSE_BASE_URL).create(ExpenseApi::class.java)

    val ledgerApi: LedgerApi =
        buildRetrofit(LEDGER_BASE_URL).create(LedgerApi::class.java)
}