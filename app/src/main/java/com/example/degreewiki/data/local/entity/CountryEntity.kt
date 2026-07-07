package com.example.degreewiki.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey val id: String,
    val slug: String,
    val name: String,
    val summary: String?,
    val imageUrl: String?,
    val offlineSavedAt: Long
)
