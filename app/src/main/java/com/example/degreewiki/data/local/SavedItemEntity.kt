package com.example.degreewiki.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_items")
data class SavedItemEntity(
    @PrimaryKey val id: String,
    val entityType: String, // e.g. "program", "university", "scholarship"
    val entityId: String,
    val title: String,
    val slug: String,
    val thumbnailUrl: String?,
    val savedAt: Long
)
