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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface DataRepository {
    val programs: Flow<List<Program>>
    val countries: Flow<List<Country>>
    val universities: Flow<List<University>>
    
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

    override val programs: Flow<List<Program>> = programDao.getAllPrograms().map { entities ->
        entities.map { it.toDomain() }
    }

    override val countries: Flow<List<Country>> = countryDao.getAllCountries().map { entities ->
        entities.map { it.toDomain() }
    }

    override val universities: Flow<List<University>> = universityDao.getAllUniversities().map { entities ->
        entities.map { it.toDomain() }
    }

    override suspend fun refreshPrograms() = withContext(Dispatchers.IO) {
        try {
            val dtos = apiService.getPrograms()
            val entities = dtos.map { it.toEntity() }
            programDao.insertPrograms(entities)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun refreshCountries() = withContext(Dispatchers.IO) {
        try {
            val dtos = apiService.getCountries()
            val entities = dtos.map { it.toEntity() }
            countryDao.insertCountries(entities)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun refreshUniversities() = withContext(Dispatchers.IO) {
        try {
            val dtos = apiService.getUniversities()
            val entities = dtos.map { it.toEntity() }
            universityDao.insertUniversities(entities)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
