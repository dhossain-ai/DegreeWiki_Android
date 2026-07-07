package com.example.degreewiki.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "programs")
data class ProgramEntity(
    @PrimaryKey val id: String,
    val slug: String,
    val title: String,
    val universityName: String,
    val countryName: String,
    val degreeLevel: String,
    val subject: String?,
    val tuition: Double?,
    val duration: String?,
    val offlineSavedAt: Long
)
