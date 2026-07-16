package com.example.degreewiki.data.local

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "saved_items",
    primaryKeys = ["ownerUserId", "savedItemId"],
    indices = [Index(value = ["ownerUserId", "programId"], unique = true)]
)
data class SavedItemEntity(
    val ownerUserId: String,
    val savedItemId: String,
    val programId: String,
    val slug: String,
    val title: String,
    val universityName: String?,
    val countryName: String?,
    val degreeLevel: String?,
    val subject: String?,
    val tuitionDisplay: String?,
    val durationMonths: Int?,
    val duration: String?,
    val savedAt: String
)
