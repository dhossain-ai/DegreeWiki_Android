package com.example.degreewiki.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class UniversityDto(
    val id: String,
    val slug: String,
    val name: String,
    val countryId: String,
    val city: String? = null,
    val logoUrl: String? = null,
    val overview: String? = null
)
