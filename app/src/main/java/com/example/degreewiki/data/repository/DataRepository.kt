package com.example.degreewiki.data.repository

import com.example.degreewiki.data.local.dao.CountryDao
import com.example.degreewiki.data.local.dao.ProgramDao
import com.example.degreewiki.data.local.dao.UniversityDao
import com.example.degreewiki.data.local.dao.ScholarshipDao
import com.example.degreewiki.data.local.dao.GuideDao
import com.example.degreewiki.data.mapper.toDomain
import com.example.degreewiki.data.mapper.toEntity
import com.example.degreewiki.data.mapper.toDomainOrNull
import com.example.degreewiki.data.network.DegreeWikiApiService
import com.example.degreewiki.domain.model.Country
import com.example.degreewiki.domain.model.Program
import com.example.degreewiki.domain.model.University
import com.example.degreewiki.domain.model.Scholarship
import com.example.degreewiki.domain.model.ScholarshipDetail
import com.example.degreewiki.domain.model.Guide
import com.example.degreewiki.domain.model.GuideDetail
import com.example.degreewiki.data.network.dto.ProgramDetailDto
import com.example.degreewiki.data.network.dto.UniversityDetailDto
import com.example.degreewiki.data.network.dto.CountryDetailDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

data class PublicRefreshState(
    val isRefreshing: Boolean = false,
    val lastRefreshFailed: Boolean = false
)

interface DataRepository {
    val programs: Flow<List<Program>>
    val countries: Flow<List<Country>>
    val universities: Flow<List<University>>
    val scholarships: Flow<List<Scholarship>>
    val guides: Flow<List<Guide>>
    val programRefreshState: StateFlow<PublicRefreshState>
    val countryRefreshState: StateFlow<PublicRefreshState>
    val universityRefreshState: StateFlow<PublicRefreshState>
    val scholarshipRefreshState: StateFlow<PublicRefreshState>
    val guideRefreshState: StateFlow<PublicRefreshState>
    
    fun getProgramById(id: String): Flow<Program?>
    fun getCountryById(id: String): Flow<Country?>
    fun getUniversityById(id: String): Flow<University?>
    fun getScholarshipBySlug(slug: String): Flow<Scholarship?>
    fun getGuideBySlug(slug: String): Flow<Guide?>
    suspend fun getProgramDetail(slug: String): ProgramDetailDto?
    suspend fun getUniversityDetail(slug: String): UniversityDetailDto?
    suspend fun getCountryDetail(slug: String): CountryDetailDto?
    suspend fun getScholarshipDetail(slug: String): ScholarshipDetail?
    suspend fun getGuideDetail(slug: String): GuideDetail?
    
    suspend fun refreshPrograms()
    suspend fun refreshCountries()
    suspend fun refreshUniversities()
    suspend fun refreshScholarships()
    suspend fun refreshGuides()
}

@Singleton
class DefaultDataRepository @Inject constructor(
    private val programDao: ProgramDao,
    private val countryDao: CountryDao,
    private val universityDao: UniversityDao,
    private val scholarshipDao: ScholarshipDao,
    private val guideDao: GuideDao,
    private val apiService: DegreeWikiApiService
) : DataRepository {
    private val _programRefreshState = MutableStateFlow(PublicRefreshState(isRefreshing = true))
    private val _countryRefreshState = MutableStateFlow(PublicRefreshState(isRefreshing = true))
    private val _universityRefreshState = MutableStateFlow(PublicRefreshState(isRefreshing = true))
    private val _scholarshipRefreshState = MutableStateFlow(PublicRefreshState(isRefreshing = true))
    private val _guideRefreshState = MutableStateFlow(PublicRefreshState(isRefreshing = true))

    override val programs: Flow<List<Program>> = programDao.getAllPrograms().map { entities ->
        entities.map { it.toDomain() }
    }

    override val countries: Flow<List<Country>> = countryDao.getAllCountries().map { entities ->
        entities.map { it.toDomain() }
    }

    override val universities: Flow<List<University>> = universityDao.getAllUniversities().map { entities ->
        entities.map { it.toDomain() }
    }
    override val scholarships: Flow<List<Scholarship>> = scholarshipDao.observeAll().map { entities -> entities.map { it.toDomain() } }
    override val guides: Flow<List<Guide>> = guideDao.observeAll().map { entities -> entities.map { it.toDomain() } }
    override val programRefreshState: StateFlow<PublicRefreshState> = _programRefreshState
    override val countryRefreshState: StateFlow<PublicRefreshState> = _countryRefreshState
    override val universityRefreshState: StateFlow<PublicRefreshState> = _universityRefreshState
    override val scholarshipRefreshState: StateFlow<PublicRefreshState> = _scholarshipRefreshState
    override val guideRefreshState: StateFlow<PublicRefreshState> = _guideRefreshState

    override fun getProgramById(id: String): Flow<Program?> = programDao.getProgramById(id).map { it?.toDomain() }

    override fun getCountryById(id: String): Flow<Country?> = countryDao.getCountryById(id).map { it?.toDomain() }

    override fun getUniversityById(id: String): Flow<University?> = universityDao.getUniversityById(id).map { it?.toDomain() }

    override fun getScholarshipBySlug(slug: String): Flow<Scholarship?> = scholarshipDao.observeBySlug(slug).map { it?.toDomain() }

    override fun getGuideBySlug(slug: String): Flow<Guide?> = guideDao.observeBySlug(slug).map { it?.toDomain() }

    override suspend fun getProgramDetail(slug: String) = safeDetail { apiService.getProgramDetail(slug).takeIf { it.ok }?.item }
    override suspend fun getUniversityDetail(slug: String) = safeDetail { apiService.getUniversityDetail(slug).takeIf { it.ok }?.item }
    override suspend fun getCountryDetail(slug: String) = safeDetail { apiService.getCountryDetail(slug).takeIf { it.ok }?.item }
    override suspend fun getScholarshipDetail(slug: String) = safeDetail { apiService.getScholarshipDetail(slug).takeIf { it.ok }?.item?.toDomainOrNull() }
    override suspend fun getGuideDetail(slug: String) = safeDetail { apiService.getGuideDetail(slug).takeIf { it.ok }?.item?.toDomainOrNull() }

    private suspend fun <T> safeDetail(block: suspend () -> T?): T? = withContext(Dispatchers.IO) {
        try { block() } catch (_: Exception) { null }
    }

    override suspend fun refreshPrograms() = withContext(Dispatchers.IO) {
        refreshInto(_programRefreshState) {
            val dtos = apiService.getPrograms()
            val entities = dtos.map { it.toEntity() }
            programDao.insertPrograms(entities)
        }
    }

    override suspend fun refreshCountries() = withContext(Dispatchers.IO) {
        refreshInto(_countryRefreshState) {
            val dtos = apiService.getCountries()
            val entities = dtos.map { it.toEntity() }
            countryDao.insertCountries(entities)
        }
    }

    override suspend fun refreshUniversities() = withContext(Dispatchers.IO) {
        refreshInto(_universityRefreshState) {
            val dtos = apiService.getUniversities()
            val entities = dtos.map { it.toEntity() }
            universityDao.insertUniversities(entities)
        }
    }

    override suspend fun refreshScholarships() = withContext(Dispatchers.IO) {
        refreshInto(_scholarshipRefreshState) {
            scholarshipDao.replaceAll(apiService.getScholarships().map { it.toEntity() })
        }
    }

    override suspend fun refreshGuides() = withContext(Dispatchers.IO) {
        refreshInto(_guideRefreshState) {
            guideDao.replaceAll(apiService.getGuides().map { it.toEntity() })
        }
    }

    private suspend fun refreshInto(
        state: MutableStateFlow<PublicRefreshState>,
        refreshBlock: suspend () -> Unit
    ) {
        state.value = PublicRefreshState(isRefreshing = true, lastRefreshFailed = false)
        try {
            refreshBlock()
            state.value = PublicRefreshState(isRefreshing = false, lastRefreshFailed = false)
        } catch (_: Exception) {
            state.value = PublicRefreshState(isRefreshing = false, lastRefreshFailed = true)
        }
    }
}
