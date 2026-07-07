package com.example.degreewiki.domain.model

data class University(
    val id: String,
    val slug: String,
    val name: String,
    val countryId: String,
    val city: String?,
    val logoUrl: String?,
    val overview: String?
)
