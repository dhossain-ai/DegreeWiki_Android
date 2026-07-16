package com.example.degreewiki.domain.model

data class SavedProgram(
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

fun List<SavedProgram>.savedItemIdByProgramId(): Map<String, String> =
    associate { it.programId to it.savedItemId }

fun List<SavedProgram>.distinctByProgramId(): List<SavedProgram> =
    distinctBy(SavedProgram::programId)
