package com.example.degreewiki.data.mapper

import com.example.degreewiki.data.local.entity.GuideEntity
import com.example.degreewiki.data.network.dto.GuideBlockDto
import com.example.degreewiki.data.network.dto.GuideCategoryDto
import com.example.degreewiki.data.network.dto.GuideDetailDto
import com.example.degreewiki.data.network.dto.GuideDto
import com.example.degreewiki.data.network.dto.GuideInlineDto
import com.example.degreewiki.data.network.dto.RelatedGuideDto
import com.example.degreewiki.domain.model.Guide
import com.example.degreewiki.domain.model.GuideBlock
import com.example.degreewiki.domain.model.GuideCategory
import com.example.degreewiki.domain.model.GuideDetail
import com.example.degreewiki.domain.model.GuideInline
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val guideCacheJson = Json { ignoreUnknownKeys = true }

fun GuideDto.toEntity(now: Long = System.currentTimeMillis()) = GuideEntity(
    id = id,
    slug = slug,
    title = title,
    summary = summary,
    categoryId = category?.id,
    categorySlug = category?.slug,
    categoryName = category?.name,
    countriesJson = guideCacheJson.encodeToString(countries),
    subjectsJson = guideCacheJson.encodeToString(subjects),
    degreeLevelsJson = guideCacheJson.encodeToString(degreeLevels),
    publishedAt = publishedAt,
    updatedAt = updatedAt,
    coverImageUrl = safeHttpUrl(coverImageUrl),
    offlineSavedAt = now
)

fun GuideEntity.toDomain() = Guide(
    id = id,
    slug = slug,
    title = title,
    summary = summary.nonBlankOrNull(),
    category = categoryName.nonBlankOrNull()?.let { GuideCategory(categoryId.nonBlankOrNull(), categorySlug.nonBlankOrNull(), it) },
    countries = decodeGuideStrings(countriesJson),
    subjects = decodeGuideStrings(subjectsJson),
    degreeLevels = decodeGuideStrings(degreeLevelsJson),
    publishedAt = publishedAt.nonBlankOrNull(),
    updatedAt = updatedAt.nonBlankOrNull(),
    coverImageUrl = safeHttpUrl(coverImageUrl)
)

fun GuideDetailDto.toDomainOrNull(): GuideDetail? {
    val validId = id.nonBlankOrNull() ?: return null
    val validSlug = slug.nonBlankOrNull() ?: return null
    val validTitle = title.nonBlankOrNull() ?: return null
    val guide = Guide(
        id = validId,
        slug = validSlug,
        title = validTitle,
        summary = summary.nonBlankOrNull(),
        category = category.toDomainOrNull(),
        countries = countries.mapNotNull { it.name.nonBlankOrNull() },
        subjects = subjects.mapNotNull { it.name.nonBlankOrNull() },
        degreeLevels = degreeLevels.mapNotNull { it.name.nonBlankOrNull() },
        publishedAt = publishedAt.nonBlankOrNull(),
        updatedAt = updatedAt.nonBlankOrNull(),
        coverImageUrl = safeHttpUrl(coverImageUrl)
    )
    return GuideDetail(
        summary = guide,
        readTimeMinutes = readTimeMinutes?.takeIf { it > 0 },
        body = body.mapNotNull(GuideBlockDto::toDomainOrNull),
        verificationStatus = verificationStatus.nonBlankOrNull(),
        lastVerifiedAt = lastVerifiedAt.nonBlankOrNull(),
        sourceConfidenceScore = sourceConfidenceScore,
        relatedGuides = relatedGuides.mapNotNull(RelatedGuideDto::toDomainOrNull)
    )
}

private fun GuideBlockDto.toDomainOrNull(): GuideBlock? = when (type?.lowercase()) {
    "heading" -> children.toInline().takeIf(List<GuideInline>::isNotEmpty)?.let { GuideBlock.Heading(level?.coerceIn(2, 4) ?: 2, it) }
    "paragraph" -> children.toInline().takeIf(List<GuideInline>::isNotEmpty)?.let(GuideBlock::Paragraph)
    "ul" -> items.map(List<GuideInlineDto>::toInline).filter(List<GuideInline>::isNotEmpty).takeIf(List<List<GuideInline>>::isNotEmpty)?.let(GuideBlock::UnorderedList)
    "ol" -> items.map(List<GuideInlineDto>::toInline).filter(List<GuideInline>::isNotEmpty).takeIf(List<List<GuideInline>>::isNotEmpty)?.let(GuideBlock::OrderedList)
    else -> null
}

private fun List<GuideInlineDto>.toInline(): List<GuideInline> = mapNotNull { inline ->
    val text = inline.text.nonBlankOrNull() ?: return@mapNotNull null
    when (inline.type?.lowercase()) {
        "text" -> GuideInline.Plain(text)
        "strong" -> GuideInline.Strong(text)
        "em" -> GuideInline.Emphasis(text)
        "link" -> safeHttpUrl(inline.href)?.let { GuideInline.Link(text, it) } ?: GuideInline.Plain(text)
        else -> null
    }
}

private fun GuideCategoryDto?.toDomainOrNull(): GuideCategory? {
    val category = this ?: return null
    return category.name.nonBlankOrNull()?.let {
        GuideCategory(category.id.nonBlankOrNull(), category.slug.nonBlankOrNull(), it)
    }
}

private fun RelatedGuideDto.toDomainOrNull(): Guide? {
    val validId = id.nonBlankOrNull() ?: return null
    val validSlug = slug.nonBlankOrNull() ?: return null
    val validTitle = title.nonBlankOrNull() ?: return null
    return Guide(validId, validSlug, validTitle, summary.nonBlankOrNull(), category.toDomainOrNull(), emptyList(), emptyList(), emptyList(), publishedAt.nonBlankOrNull(), null, safeHttpUrl(coverImageUrl))
}

private fun decodeGuideStrings(value: String): List<String> = runCatching {
    guideCacheJson.decodeFromString<List<String>>(value).mapNotNull(String::nonBlankOrNull)
}.getOrDefault(emptyList())
