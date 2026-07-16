package com.example.degreewiki.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class SavedItemsResponseDto(
    val ok: Boolean,
    val items: List<SavedProgramDto>
)

@Serializable
data class SavedProgramDto(
    val savedItemId: String,
    val entityType: String,
    val entityId: String,
    val savedAt: String,
    val program: SavedProgramSummaryDto
)

@Serializable
data class SavedProgramSummaryDto(
    val id: String,
    val slug: String,
    val title: String,
    val universityName: String?,
    val countryName: String?,
    val degreeLevel: String?,
    val subject: String?,
    val tuitionDisplay: String?,
    val durationMonths: Int?,
    val duration: String?
)

@Serializable
data class SaveProgramRequestDto(
    val entityType: String = "program",
    val entityId: String
)

@Serializable
data class SaveProgramResponseDto(
    val ok: Boolean,
    val saved: Boolean,
    val item: SavedProgramMutationDto
)

@Serializable
data class SavedProgramMutationDto(
    val savedItemId: String,
    val entityType: String,
    val entityId: String,
    val savedAt: String
)

@Serializable
data class DeleteSavedItemResponseDto(
    val ok: Boolean,
    val saved: Boolean
)
