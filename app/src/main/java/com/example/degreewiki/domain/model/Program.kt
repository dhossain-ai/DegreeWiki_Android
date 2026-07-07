package com.example.degreewiki.domain.model

data class Program(
    val id: String,
    val slug: String,
    val title: String,
    val universityName: String,
    val countryName: String,
    val degreeLevel: String,
    val subject: String?,
    val tuition: Double?,
    val duration: String?
)
