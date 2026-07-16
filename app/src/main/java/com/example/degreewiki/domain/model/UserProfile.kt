package com.example.degreewiki.domain.model

data class UserProfile(
    val userId: String,
    val email: String?,
    val authDisplayName: String?,
    val createdAt: String?,
    val profile: ProfileDetails?,
    val savedProgramCount: Int
) {
    val visibleIdentity: String
        get() = profile?.displayName?.takeIf(String::isNotBlank)
            ?: authDisplayName?.takeIf(String::isNotBlank)
            ?: email?.takeIf(String::isNotBlank)
            ?: "DegreeWiki account"
}

data class ProfileDetails(
    val displayName: String?,
    val avatarUrl: String?,
    val accountStatus: String?
)
