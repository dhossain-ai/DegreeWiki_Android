package com.example.degreewiki.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProgramDto(
    val id: String,
    val slug: String,
    val title: String,
    val universityName: String,
    val countryName: String,
    val degreeLevel: String,
    val subject: String? = null,
    val tuition: Double? = null,
    val duration: String? = null
)
