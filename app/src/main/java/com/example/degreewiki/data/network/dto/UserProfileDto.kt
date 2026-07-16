package com.example.degreewiki.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileResponseDto(
    val ok: Boolean,
    val user: AuthenticatedUserDto,
    val profile: UserProfileDto?,
    val savedSummary: SavedSummaryDto
)

@Serializable
data class AuthenticatedUserDto(
    val id: String,
    val email: String?,
    val displayName: String?,
    val createdAt: String?
)

@Serializable
data class UserProfileDto(
    val displayName: String?,
    val avatarUrl: String?,
    val accountStatus: String?
)

@Serializable
data class SavedSummaryDto(
    val programCount: Int
)
