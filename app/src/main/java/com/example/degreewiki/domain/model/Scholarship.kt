package com.example.degreewiki.domain.model

data class Scholarship(
    val id: String,
    val slug: String,
    val title: String,
    val providerName: String?,
    val summary: String?,
    val scholarshipTypeLabel: String?,
    val fundingTypeLabel: String?,
    val amountDisplay: String?,
    val deadline: String?,
    val deadlineText: String?,
    val deadlineDisplay: String?,
    val studyCountries: List<String>,
    val degreeLevels: List<String>,
    val subjects: List<String>,
    val officialUrl: String?,
    val applicationUrl: String?,
    val verificationStatus: String?,
    val lastVerifiedAt: String?,
    val imageUrl: String?
)

data class ScholarshipDetail(
    val summary: Scholarship,
    val overview: String?,
    val eligibilitySummary: String?,
    val coverageNotes: String?,
    val studyCountries: List<NamedRelation>,
    val degreeLevels: List<NamedRelation>,
    val subjects: List<NamedRelation>,
    val eligibleNationalities: List<NationalityEligibility>,
    val universities: List<NamedRelation>,
    val programs: List<ProgramRelation>,
    val providerUrl: String?,
    val sourceConfidenceScore: Int?,
    val updatedAt: String?,
    val logoUrl: String?,
    val coverImageUrl: String?
)

data class NamedRelation(val id: String?, val slug: String?, val name: String, val code: String?)
data class NationalityEligibility(val country: NamedRelation, val type: String?, val notes: String?)
data class ProgramRelation(val id: String?, val slug: String?, val title: String)
