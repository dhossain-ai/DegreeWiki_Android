package com.example.degreewiki.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scholarships")
data class ScholarshipEntity(
    @PrimaryKey val id: String,
    val slug: String,
    val title: String,
    val providerName: String?,
    val summary: String?,
    val scholarshipTypeLabel: String?,
    val fundingTypeLabel: String?,
    val amountDisplay: String?,
    val deadline: String?,
    val deadlineText: String?,
    val deadlineDisplay: String?,
    val studyCountriesJson: String,
    val degreeLevelsJson: String,
    val subjectsJson: String,
    val officialUrl: String?,
    val applicationUrl: String?,
    val verificationStatus: String?,
    val lastVerifiedAt: String?,
    val imageUrl: String?,
    val offlineSavedAt: Long
)
