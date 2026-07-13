package com.example.degreewiki.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "guides")
data class GuideEntity(
    @PrimaryKey val id: String,
    val slug: String,
    val title: String,
    val summary: String?,
    val categoryId: String?,
    val categorySlug: String?,
    val categoryName: String?,
    val countriesJson: String,
    val subjectsJson: String,
    val degreeLevelsJson: String,
    val publishedAt: String?,
    val updatedAt: String?,
    val coverImageUrl: String?,
    val offlineSavedAt: Long
)
