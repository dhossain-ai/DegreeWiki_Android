package com.example.degreewiki.domain.model

data class SavedItem(
    val id: String,
    val entityType: String,
    val entityId: String,
    val title: String,
    val slug: String,
    val thumbnailUrl: String?,
    val savedAt: Long
)
