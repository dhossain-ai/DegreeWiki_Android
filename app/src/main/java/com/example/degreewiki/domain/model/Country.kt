package com.example.degreewiki.domain.model

data class Country(
    val id: String,
    val slug: String,
    val name: String,
    val summary: String?,
    val imageUrl: String?
)
