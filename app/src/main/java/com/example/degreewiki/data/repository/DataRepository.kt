package com.example.degreewiki.data.repository

import com.example.degreewiki.data.local.dao.CountryDao
import com.example.degreewiki.data.local.dao.ProgramDao
import com.example.degreewiki.data.local.dao.UniversityDao
import com.example.degreewiki.data.mapper.toDomain
import com.example.degreewiki.data.mapper.toEntity
import com.example.degreewiki.data.network.DegreeWikiApiService
import com.example.degreewiki.domain.model.Country
import com.example.degreewiki.domain.model.Program
import com.example.degreewiki.domain.model.University
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
    val programRefreshState: StateFlow<PublicRefreshState>
    val countryRefreshState: StateFlow<PublicRefreshState>
    val universityRefreshState: StateFlow<PublicRefreshState>
    
    fun getProgramById(id: String): Flow<Program?>
    fun getCountryById(id: String): Flow<Country?>
    fun getUniversityById(id: String): Flow<University?>
    
    suspend fun refreshPrograms()
    suspend fun refreshCountries()
    suspend fun refreshUniversities()
}

@Singleton
class DefaultDataRepository @Inject constructor(
    private val programDao: ProgramDao,
    private val countryDao: CountryDao,
    private val universityDao: UniversityDao,
    private val apiService: DegreeWikiApiService
) : DataRepository {
    private val _programRefreshState = MutableStateFlow(PublicRefreshState(isRefreshing = true))
    private val _countryRefreshState = MutableStateFlow(PublicRefreshState(isRefreshing = true))
    private val _universityRefreshState = MutableStateFlow(PublicRefreshState(isRefreshing = true))

    override val programs: Flow<List<Program>> = programDao.getAllPrograms().map { entities ->
        entities.map { it.toDomain() }
    }

    override val countries: Flow<List<Country>> = countryDao.getAllCountries().map { entities ->
        entities.map { it.toDomain() }
    }

    override val universities: Flow<List<University>> = universityDao.getAllUniversities().map { entities ->
        entities.map { it.toDomain() }
    }
    override val programRefreshState: StateFlow<PublicRefreshState> = _programRefreshState
    override val countryRefreshState: StateFlow<PublicRefreshState> = _countryRefreshState
    override val universityRefreshState: StateFlow<PublicRefreshState> = _universityRefreshState

    override fun getProgramById(id: String): Flow<Program?> = programDao.getProgramById(id).map { it?.toDomain() }

    override fun getCountryById(id: String): Flow<Country?> = countryDao.getCountryById(id).map { it?.toDomain() }

    override fun getUniversityById(id: String): Flow<University?> = universityDao.getUniversityById(id).map { it?.toDomain() }

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
