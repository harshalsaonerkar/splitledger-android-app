package com.splitledger.app.data.api

import com.splitledger.app.data.model.AddMemberRequest
import com.splitledger.app.data.model.CreateGroupRequest
import com.splitledger.app.data.model.GroupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface GroupApi {

    @GET("api/groups/my")
    suspend fun getMyGroups(
        @Header("Authorization") token: String
    ): Response<List<GroupResponse>>

    @GET("api/groups/{groupId}")
    suspend fun getGroup(
        @Header("Authorization") token: String,
        @Path("groupId") groupId: String
    ): Response<GroupResponse>

    @POST("api/groups")
    suspend fun createGroup(
        @Header("Authorization") token: String,
        @Body request: CreateGroupRequest
    ): Response<GroupResponse>

    @POST("api/groups/{groupId}/members")
    suspend fun addMember(
        @Header("Authorization") token: String,
        @Path("groupId") groupId: String,
        @Body request: AddMemberRequest
    ): Response<GroupResponse>
}