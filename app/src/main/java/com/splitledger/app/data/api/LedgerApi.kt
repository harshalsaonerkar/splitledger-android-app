package com.splitledger.app.data.api

import com.splitledger.app.data.model.BalanceResponse
import com.splitledger.app.data.model.SettlementResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface LedgerApi {

    @GET("api/ledger/group/{groupId}/balances")
    suspend fun getBalances(
        @Header("Authorization") token: String,
        @Path("groupId") groupId: String
    ): Response<List<BalanceResponse>>

    @GET("api/ledger/group/{groupId}/settlements")
    suspend fun getSettlements(
        @Header("Authorization") token: String,
        @Path("groupId") groupId: String
    ): Response<List<SettlementResponse>>
}