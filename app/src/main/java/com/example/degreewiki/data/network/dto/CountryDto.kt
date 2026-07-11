package com.example.degreewiki.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CountryDto(
    val id: String,
    val slug: String,
    val name: String,
    val summary: String? = null,
    val imageUrl: String? = null
    ,val iso2: String? = null, val continent: String? = null,
    val currencyCode: String? = null, val currencyName: String? = null,
    val tuitionOverview: String? = null, val livingCostOverview: String? = null,
    val verificationStatus: String? = null, val lastVerifiedAt: String? = null
)
