package com.example.degreewiki.data.network

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

@Serializable
data class BootstrapResponse(
    val ok: Boolean,
    val siteName: String,
    val featureFlags: Map<String, Boolean>
)

@Serializable
data class ProgramSearchResponse(
    val items: List<ProgramSummaryDto>,
    val page: Int,
    val pageSize: Int,
    val total: Int,
    val nextPage: Int?
)

@Serializable
data class ProgramSummaryDto(
    val id: String,
    val title: String,
    val slug: String,
    val universityName: String,
    val countryName: String,
    val degreeLevel: String,
    val tuitionFee: Double?,
    val currency: String?,
    val thumbnailUrl: String?
)

interface DegreeWikiApiService {
    @GET("api/mobile/bootstrap")
    suspend fun getBootstrapData(): BootstrapResponse

    @GET("api/mobile/programs/search")
    suspend fun searchPrograms(
        @Query("q") query: String?,
        @Query("page") page: Int?
    ): ProgramSearchResponse

    @GET("api/mobile/programs")
    suspend fun getPrograms(): List<com.example.degreewiki.data.network.dto.ProgramDto>

    @GET("api/mobile/countries")
    suspend fun getCountries(): List<com.example.degreewiki.data.network.dto.CountryDto>

    @GET("api/mobile/universities")
    suspend fun getUniversities(): List<com.example.degreewiki.data.network.dto.UniversityDto>

    @GET("api/mobile/me")
    suspend fun getProfile(): com.example.degreewiki.data.network.dto.UserProfileDto

    @GET("api/mobile/me/saved-items")
    suspend fun getSavedItems(): List<com.example.degreewiki.data.network.dto.SavedItemDto>

    @retrofit2.http.DELETE("api/mobile/me/saved-items/{id}")
    suspend fun deleteSavedItem(@retrofit2.http.Path("id") id: String)
}
