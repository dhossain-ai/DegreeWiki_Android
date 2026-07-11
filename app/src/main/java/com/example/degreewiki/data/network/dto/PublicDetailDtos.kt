package com.example.degreewiki.data.network.dto

import kotlinx.serialization.Serializable

@Serializable data class DetailResponse<T>(val ok: Boolean = false, val item: T? = null)
@Serializable data class NamedRef(val id: String? = null, val slug: String? = null, val name: String? = null, val code: String? = null, val shortLabel: String? = null)
@Serializable data class MoneyDetail(val minAmount: Double? = null, val maxAmount: Double? = null, val currency: String? = null, val period: String? = null, val notes: String? = null, val display: String? = null, val detail: String? = null)
@Serializable data class IntakeDto(val name: String? = null, val intake: String? = null, val deadline: String? = null, val deadlineText: String? = null)

@Serializable data class ProgramDetailDto(
    val id: String? = null, val slug: String? = null, val title: String? = null,
    val university: NamedRef? = null, val country: NamedRef? = null, val city: NamedRef? = null,
    val location: String? = null, val degreeLevel: NamedRef? = null, val subject: NamedRef? = null,
    val degreeAward: String? = null, val duration: String? = null, val durationMonths: Int? = null,
    val language: String? = null, val studyModeLabel: String? = null, val deliveryModeLabel: String? = null,
    val tuition: MoneyDetail? = null, val nextIntake: String? = null, val nextDeadline: String? = null,
    val intakes: List<IntakeDto> = emptyList(), val admissionRequirements: String? = null,
    val englishRequirements: String? = null, val gpaRequirements: String? = null,
    val documents: String? = null, val curriculumSummary: String? = null, val careerOutcomes: String? = null,
    val officialUrl: String? = null, val applicationUrl: String? = null,
    val verificationStatus: String? = null, val lastVerifiedAt: String? = null
)

@Serializable data class UniversityDetailDto(
    val id: String? = null, val slug: String? = null, val name: String? = null,
    val shortName: String? = null, val nativeName: String? = null, val institutionType: String? = null,
    val ownershipType: String? = null, val foundedYear: Int? = null, val studentCount: Int? = null,
    val country: NamedRef? = null, val city: NamedRef? = null, val overview: String? = null,
    val officialUrl: String? = null, val admissionsUrl: String? = null, val internationalAdmissionsUrl: String? = null,
    val rankingSummary: String? = null, val campusSummary: String? = null, val admissionOverview: String? = null,
    val applicationOverview: String? = null, val languageRequirementsSummary: String? = null,
    val scholarshipsSummary: String? = null, val housingSummary: String? = null,
    val studentLifeSummary: String? = null, val internationalStudentSummary: String? = null,
    val careerSupportSummary: String? = null, val verificationStatus: String? = null,
    val lastVerifiedAt: String? = null, val relatedPrograms: List<ProgramDto> = emptyList()
)

@Serializable data class FaqDto(val question: String? = null, val answer: String? = null)
@Serializable data class CountryDetailDto(
    val id: String? = null, val slug: String? = null, val name: String? = null, val iso2: String? = null,
    val iso3: String? = null, val continent: String? = null, val overview: String? = null,
    val destinationSummary: String? = null, val capitalCityName: String? = null,
    val currencyCode: String? = null, val currencyName: String? = null,
    val officialLanguages: List<String> = emptyList(), val commonStudyLanguages: List<String> = emptyList(),
    val popularStudentCities: List<String> = emptyList(), val tuitionOverview: String? = null,
    val livingCostOverview: String? = null, val admissionOverview: String? = null, val visaOverview: String? = null,
    val studentWorkRightsOverview: String? = null, val postStudyWorkOverview: String? = null,
    val scholarshipSummary: String? = null, val universitySystemOverview: String? = null,
    val requiredDocumentsOverview: String? = null, val intakeOverview: String? = null,
    val officialEducationUrl: String? = null, val officialVisaUrl: String? = null,
    val faq: List<FaqDto> = emptyList(), val verificationStatus: String? = null, val lastVerifiedAt: String? = null,
    val relatedUniversities: List<UniversityDto> = emptyList(), val relatedPrograms: List<ProgramDto> = emptyList()
)
