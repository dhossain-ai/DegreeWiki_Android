package com.example.degreewiki.domain.model

data class GuideCategory(val id: String?, val slug: String?, val name: String)

data class Guide(
    val id: String,
    val slug: String,
    val title: String,
    val summary: String?,
    val category: GuideCategory?,
    val countries: List<String>,
    val subjects: List<String>,
    val degreeLevels: List<String>,
    val publishedAt: String?,
    val updatedAt: String?,
    val coverImageUrl: String?
)

sealed interface GuideInline {
    val text: String
    data class Plain(override val text: String) : GuideInline
    data class Strong(override val text: String) : GuideInline
    data class Emphasis(override val text: String) : GuideInline
    data class Link(override val text: String, val href: String) : GuideInline
}

sealed interface GuideBlock {
    data class Heading(val level: Int, val children: List<GuideInline>) : GuideBlock
    data class Paragraph(val children: List<GuideInline>) : GuideBlock
    data class UnorderedList(val items: List<List<GuideInline>>) : GuideBlock
    data class OrderedList(val items: List<List<GuideInline>>) : GuideBlock
}

data class GuideDetail(
    val summary: Guide,
    val readTimeMinutes: Int?,
    val body: List<GuideBlock>,
    val verificationStatus: String?,
    val lastVerifiedAt: String?,
    val sourceConfidenceScore: Int?,
    val relatedGuides: List<Guide>
)
