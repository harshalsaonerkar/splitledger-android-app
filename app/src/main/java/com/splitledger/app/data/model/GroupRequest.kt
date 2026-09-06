package com.splitledger.app.data.model

data class CreateGroupRequest(
    val name: String,
    val description: String
)

data class AddMemberRequest(
    val email: String
)
