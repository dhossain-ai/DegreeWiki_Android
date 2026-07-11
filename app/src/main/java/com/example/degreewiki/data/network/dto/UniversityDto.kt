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
    ,val shortName: String? = null, val nativeName: String? = null,
    val countryName: String? = null, val countryCode: String? = null,
    val officialUrl: String? = null, val verificationStatus: String? = null,
    val lastVerifiedAt: String? = null, val rankingSummary: String? = null,
    val rankingSummaryTeaser: String? = null, val imageUrl: String? = null,
    val overviewTeaser: String? = null
)
