package com.example.degreewiki.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val id: String,
    val email: String,
    val name: String,
    val role: String
)
