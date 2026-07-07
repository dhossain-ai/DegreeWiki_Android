package com.example.degreewiki.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CountryDto(
    val id: String,
    val slug: String,
    val name: String,
    val summary: String? = null,
    val imageUrl: String? = null
)
