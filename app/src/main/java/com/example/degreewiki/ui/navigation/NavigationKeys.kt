package com.example.degreewiki.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Main : NavKey

@Serializable
data object Login : NavKey

@Serializable
data class ProgramDetail(val id: String) : NavKey

@Serializable
data class UniversityDetail(val id: String) : NavKey

@Serializable
data class CountryDetail(val id: String) : NavKey
