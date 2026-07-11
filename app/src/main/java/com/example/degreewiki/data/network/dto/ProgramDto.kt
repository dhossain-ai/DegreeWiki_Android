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
    ,val universityId: String? = null, val universitySlug: String? = null,
    val countryId: String? = null, val countryCode: String? = null, val city: String? = null,
    val location: String? = null, val degreeLevelCode: String? = null,
    val degreeLevelShortLabel: String? = null, val tuitionMinAmount: Double? = null,
    val tuitionMaxAmount: Double? = null, val tuitionCurrency: String? = null,
    val tuitionPeriod: String? = null, val tuitionDisplay: String? = null,
    val durationMonths: Int? = null, val language: String? = null,
    val studyMode: String? = null, val deliveryMode: String? = null,
    val officialUrl: String? = null, val verificationStatus: String? = null,
    val lastVerifiedAt: String? = null, val imageUrl: String? = null
)
