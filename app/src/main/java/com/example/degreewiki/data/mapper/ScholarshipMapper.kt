package com.example.degreewiki.data.mapper

import com.example.degreewiki.data.local.entity.ScholarshipEntity
import com.example.degreewiki.data.network.dto.NamedRef
import com.example.degreewiki.data.network.dto.ScholarshipDetailDto
import com.example.degreewiki.data.network.dto.ScholarshipDto
import com.example.degreewiki.domain.model.NamedRelation
import com.example.degreewiki.domain.model.NationalityEligibility
import com.example.degreewiki.domain.model.ProgramRelation
import com.example.degreewiki.domain.model.Scholarship
import com.example.degreewiki.domain.model.ScholarshipDetail
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val cacheJson = Json { ignoreUnknownKeys = true }

fun ScholarshipDto.toEntity(now: Long = System.currentTimeMillis()) = ScholarshipEntity(
    id = id,
    slug = slug,
    title = name,
    providerName = providerName,
    summary = summary,
    scholarshipTypeLabel = scholarshipTypeLabel,
    fundingTypeLabel = fundingTypeLabel,
    amountDisplay = amountDisplay,
    deadline = deadline,
    deadlineText = deadlineText,
    deadlineDisplay = deadlineDisplay,
    studyCountriesJson = cacheJson.encodeToString(studyCountries),
    degreeLevelsJson = cacheJson.encodeToString(eligibleDegreeLevels),
    subjectsJson = cacheJson.encodeToString(eligibleSubjects),
    officialUrl = safeHttpUrl(officialUrl),
    applicationUrl = safeHttpUrl(applicationUrl),
    verificationStatus = verificationStatus,
    lastVerifiedAt = lastVerifiedAt,
    imageUrl = safeHttpUrl(imageUrl),
    offlineSavedAt = now
)

fun ScholarshipEntity.toDomain() = Scholarship(
    id = id,
    slug = slug,
    title = title,
    providerName = providerName.nonBlankOrNull(),
    summary = summary.nonBlankOrNull(),
    scholarshipTypeLabel = scholarshipTypeLabel.nonBlankOrNull(),
    fundingTypeLabel = fundingTypeLabel.nonBlankOrNull(),
    amountDisplay = amountDisplay.nonBlankOrNull(),
    deadline = deadline.nonBlankOrNull(),
    deadlineText = deadlineText.nonBlankOrNull(),
    deadlineDisplay = deadlineDisplay.nonBlankOrNull(),
    studyCountries = decodeStrings(studyCountriesJson),
    degreeLevels = decodeStrings(degreeLevelsJson),
    subjects = decodeStrings(subjectsJson),
    officialUrl = safeHttpUrl(officialUrl),
    applicationUrl = safeHttpUrl(applicationUrl),
    verificationStatus = verificationStatus.nonBlankOrNull(),
    lastVerifiedAt = lastVerifiedAt.nonBlankOrNull(),
    imageUrl = safeHttpUrl(imageUrl)
)

fun ScholarshipDetailDto.toDomainOrNull(): ScholarshipDetail? {
    val validId = id.nonBlankOrNull() ?: return null
    val validSlug = slug.nonBlankOrNull() ?: return null
    val validName = name.nonBlankOrNull() ?: return null
    val summaryModel = Scholarship(
        id = validId,
        slug = validSlug,
        title = validName,
        providerName = providerName.nonBlankOrNull(),
        summary = summary.nonBlankOrNull(),
        scholarshipTypeLabel = scholarshipTypeLabel.nonBlankOrNull(),
        fundingTypeLabel = fundingTypeLabel.nonBlankOrNull(),
        amountDisplay = amount?.display.nonBlankOrNull(),
        deadline = deadline?.date.nonBlankOrNull(),
        deadlineText = deadline?.text.nonBlankOrNull(),
        deadlineDisplay = deadline?.display.nonBlankOrNull(),
        studyCountries = studyCountries.mapNotNull { it.name.nonBlankOrNull() },
        degreeLevels = eligibleDegreeLevels.mapNotNull { it.name.nonBlankOrNull() },
        subjects = eligibleSubjects.mapNotNull { it.name.nonBlankOrNull() },
        officialUrl = safeHttpUrl(officialUrl),
        applicationUrl = safeHttpUrl(applicationUrl),
        verificationStatus = verificationStatus.nonBlankOrNull(),
        lastVerifiedAt = lastVerifiedAt.nonBlankOrNull(),
        imageUrl = safeHttpUrl(imageUrl ?: coverImageUrl ?: logoUrl)
    )
    return ScholarshipDetail(
        summary = summaryModel,
        overview = overview.nonBlankOrNull(),
        eligibilitySummary = eligibilitySummary.nonBlankOrNull(),
        coverageNotes = coverageNotes.nonBlankOrNull(),
        studyCountries = studyCountries.mapNotNull(NamedRef::toNamedRelation),
        degreeLevels = eligibleDegreeLevels.mapNotNull(NamedRef::toNamedRelation),
        subjects = eligibleSubjects.mapNotNull(NamedRef::toNamedRelation),
        eligibleNationalities = eligibleNationalities.mapNotNull { item ->
            item.country?.toNamedRelation()?.let { NationalityEligibility(it, item.eligibilityType.nonBlankOrNull(), item.notes.nonBlankOrNull()) }
        },
        universities = universities.mapNotNull(NamedRef::toNamedRelation),
        programs = programs.mapNotNull { item -> item.title.nonBlankOrNull()?.let { ProgramRelation(item.id.nonBlankOrNull(), item.slug.nonBlankOrNull(), it) } },
        providerUrl = safeHttpUrl(providerUrl),
        sourceConfidenceScore = sourceConfidenceScore,
        updatedAt = updatedAt.nonBlankOrNull(),
        logoUrl = safeHttpUrl(logoUrl),
        coverImageUrl = safeHttpUrl(coverImageUrl)
    )
}

internal fun String?.nonBlankOrNull(): String? = this?.trim()?.takeIf(String::isNotEmpty)

internal fun safeHttpUrl(value: String?): String? = runCatching {
    val uri = java.net.URI(value.nonBlankOrNull() ?: return null)
    value?.takeIf { uri.scheme.equals("http", true) || uri.scheme.equals("https", true) }
}.getOrNull()

private fun NamedRef.toNamedRelation(): NamedRelation? = name.nonBlankOrNull()?.let {
    NamedRelation(id.nonBlankOrNull(), slug.nonBlankOrNull(), it, code.nonBlankOrNull())
}

private fun decodeStrings(value: String): List<String> = runCatching {
    cacheJson.decodeFromString<List<String>>(value).mapNotNull(String::nonBlankOrNull)
}.getOrDefault(emptyList())
