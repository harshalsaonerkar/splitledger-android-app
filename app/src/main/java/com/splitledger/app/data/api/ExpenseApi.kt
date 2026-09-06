package com.splitledger.app.data.api

import com.splitledger.app.data.model.CreateExpenseRequest
import com.splitledger.app.data.model.ExpenseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ExpenseApi {

    @POST("api/expenses")
    suspend fun createExpense(
        @Header("Authorization") token: String,
        @Body request: CreateExpenseRequest
    ): Response<ExpenseResponse>

    @GET("api/expenses/group/{groupId}")
    suspend fun getGroupExpenses(
        @Header("Authorization") token: String,
        @Path("groupId") groupId: String
    ): Response<List<ExpenseResponse>>
}