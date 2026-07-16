package com.example.degreewiki.data.network

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Headers
import retrofit2.http.POST
import com.example.degreewiki.data.network.dto.*

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

    @GET("api/mobile/programs/{slug}") suspend fun getProgramDetail(@Path("slug") slug: String): DetailResponse<ProgramDetailDto>
    @GET("api/mobile/universities/{slug}") suspend fun getUniversityDetail(@Path("slug") slug: String): DetailResponse<UniversityDetailDto>
    @GET("api/mobile/countries/{slug}") suspend fun getCountryDetail(@Path("slug") slug: String): DetailResponse<CountryDetailDto>

    @GET("api/mobile/scholarships")
    suspend fun getScholarships(): List<ScholarshipDto>

    @GET("api/mobile/scholarships/{slug}")
    suspend fun getScholarshipDetail(@Path("slug") slug: String): DetailResponse<ScholarshipDetailDto>

    @GET("api/mobile/guides")
    suspend fun getGuides(): List<GuideDto>

    @GET("api/mobile/guides/{slug}")
    suspend fun getGuideDetail(@Path("slug") slug: String): DetailResponse<GuideDetailDto>

    @Headers("$AUTHENTICATED_REQUEST_HEADER: true")
    @GET("api/mobile/me")
    suspend fun getProfile(): UserProfileResponseDto

    @Headers("$AUTHENTICATED_REQUEST_HEADER: true")
    @GET("api/mobile/me/saved-items")
    suspend fun getSavedItems(): SavedItemsResponseDto

    @Headers("$AUTHENTICATED_REQUEST_HEADER: true")
    @POST("api/mobile/me/saved-items")
    suspend fun saveProgram(@Body request: SaveProgramRequestDto): SaveProgramResponseDto

    @Headers("$AUTHENTICATED_REQUEST_HEADER: true")
    @DELETE("api/mobile/me/saved-items/{savedItemId}")
    suspend fun deleteSavedItem(@Path("savedItemId") savedItemId: String): DeleteSavedItemResponseDto
}
