package com.example.degreewiki.data.network.dto

import kotlinx.serialization.Serializable

@Serializable data class GuideCategoryDto(val id: String? = null, val slug: String? = null, val name: String? = null)

@Serializable
data class GuideDto(
    val id: String,
    val slug: String,
    val title: String,
    val summary: String? = null,
    val category: GuideCategoryDto? = null,
    val countries: List<String> = emptyList(),
    val subjects: List<String> = emptyList(),
    val degreeLevels: List<String> = emptyList(),
    val publishedAt: String? = null,
    val updatedAt: String? = null,
    val coverImageUrl: String? = null
)

@Serializable
data class GuideInlineDto(
    val type: String? = null,
    val text: String? = null,
    val href: String? = null
)

@Serializable
data class GuideBlockDto(
    val type: String? = null,
    val level: Int? = null,
    val children: List<GuideInlineDto> = emptyList(),
    val items: List<List<GuideInlineDto>> = emptyList()
)

@Serializable data class GuideSectionHeadingDto(val level: Int? = null, val text: String? = null)

@Serializable
data class RelatedGuideDto(
    val id: String? = null,
    val slug: String? = null,
    val title: String? = null,
    val summary: String? = null,
    val category: GuideCategoryDto? = null,
    val publishedAt: String? = null,
    val coverImageUrl: String? = null
)

@Serializable
data class GuideDetailDto(
    val id: String? = null,
    val slug: String? = null,
    val title: String? = null,
    val summary: String? = null,
    val category: GuideCategoryDto? = null,
    val countries: List<NamedRef> = emptyList(),
    val subjects: List<NamedRef> = emptyList(),
    val degreeLevels: List<NamedRef> = emptyList(),
    val publishedAt: String? = null,
    val updatedAt: String? = null,
    val readTimeMinutes: Int? = null,
    val contentFormat: String? = null,
    val body: List<GuideBlockDto> = emptyList(),
    val sectionHeadings: List<GuideSectionHeadingDto> = emptyList(),
    val coverImageUrl: String? = null,
    val verificationStatus: String? = null,
    val lastVerifiedAt: String? = null,
    val sourceConfidenceScore: Int? = null,
    val relatedGuides: List<RelatedGuideDto> = emptyList()
)
