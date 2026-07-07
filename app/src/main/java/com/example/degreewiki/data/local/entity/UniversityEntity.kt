package com.example.degreewiki.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "universities")
data class UniversityEntity(
    @PrimaryKey val id: String,
    val slug: String,
    val name: String,
    val countryId: String,
    val city: String?,
    val logoUrl: String?,
    val overview: String?,
    val offlineSavedAt: Long
)
