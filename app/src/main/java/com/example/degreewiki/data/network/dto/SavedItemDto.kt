package com.example.degreewiki.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class SavedItemDto(
    val id: String,
    val entityType: String,
    val entityId: String,
    val title: String,
    val slug: String,
    val thumbnail: String?,
    val savedAt: Long
)
