package com.splitledger.app.data.model

data class GroupResponse(
    val id: String,
    val name: String,
    val description: String?,
    val inviteCode: String,
    val createdBy: String,
    val members: List<MemberDto>
)

data class MemberDto(
    val userId: String,
    val userName: String,
    val userEmail: String,
    val role: String
)
