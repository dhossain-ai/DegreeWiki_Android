package com.example.degreewiki.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ScholarshipDto(
    val id: String,
    val slug: String,
    val name: String,
    val providerName: String? = null,
    val providerType: String? = null,
    val providerTypeLabel: String? = null,
    val summary: String? = null,
    val scholarshipType: String? = null,
    val scholarshipTypeLabel: String? = null,
    val fundingType: String? = null,
    val fundingTypeLabel: String? = null,
    val applicationType: String? = null,
    val amountMin: Double? = null,
    val amountMax: Double? = null,
    val currency: String? = null,
    val amountDisplay: String? = null,
    val deadline: String? = null,
    val deadlineText: String? = null,
    val deadlineDisplay: String? = null,
    val studyCountries: List<String> = emptyList(),
    val eligibleDegreeLevels: List<String> = emptyList(),
    val eligibleSubjects: List<String> = emptyList(),
    val officialUrl: String? = null,
    val applicationUrl: String? = null,
    val verificationStatus: String? = null,
    val lastVerifiedAt: String? = null,
    val updatedAt: String? = null,
    val imageUrl: String? = null
)

@Serializable data class ScholarshipAmountDto(val min: Double? = null, val max: Double? = null, val currency: String? = null, val display: String? = null)
@Serializable data class ScholarshipDeadlineDto(val date: String? = null, val text: String? = null, val display: String? = null)
@Serializable data class ScholarshipNationalityDto(val country: NamedRef? = null, val eligibilityType: String? = null, val notes: String? = null)
@Serializable data class ScholarshipProgramDto(val id: String? = null, val slug: String? = null, val title: String? = null)

@Serializable
data class ScholarshipDetailDto(
    val id: String? = null,
    val slug: String? = null,
    val name: String? = null,
    val providerName: String? = null,
    val providerType: String? = null,
    val providerTypeLabel: String? = null,
    val summary: String? = null,
    val scholarshipType: String? = null,
    val scholarshipTypeLabel: String? = null,
    val fundingType: String? = null,
    val fundingTypeLabel: String? = null,
    val applicationType: String? = null,
    val applicationTypeLabel: String? = null,
    val overview: String? = null,
    val contentFormat: String? = null,
    val eligibilitySummary: String? = null,
    val coverageNotes: String? = null,
    val amount: ScholarshipAmountDto? = null,
    val deadline: ScholarshipDeadlineDto? = null,
    val studyCountries: List<NamedRef> = emptyList(),
    val eligibleDegreeLevels: List<NamedRef> = emptyList(),
    val eligibleSubjects: List<NamedRef> = emptyList(),
    val eligibleNationalities: List<ScholarshipNationalityDto> = emptyList(),
    val universities: List<NamedRef> = emptyList(),
    val programs: List<ScholarshipProgramDto> = emptyList(),
    val officialUrl: String? = null,
    val applicationUrl: String? = null,
    val providerUrl: String? = null,
    val verificationStatus: String? = null,
    val lastVerifiedAt: String? = null,
    val sourceConfidenceScore: Int? = null,
    val updatedAt: String? = null,
    val logoUrl: String? = null,
    val coverImageUrl: String? = null,
    val imageUrl: String? = null
)
