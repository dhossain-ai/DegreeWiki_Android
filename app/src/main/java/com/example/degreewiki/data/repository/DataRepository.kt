package com.example.degreewiki.data.repository

import com.example.degreewiki.data.local.dao.ProgramDao
import com.example.degreewiki.data.mapper.toDomain
import com.example.degreewiki.data.mapper.toEntity
import com.example.degreewiki.data.network.DegreeWikiApiService
import com.example.degreewiki.domain.model.Program
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface DataRepository {
    val programs: Flow<List<Program>>
    suspend fun refreshPrograms()
}

@Singleton
class DefaultDataRepository @Inject constructor(
    private val programDao: ProgramDao,
    private val apiService: DegreeWikiApiService
) : DataRepository {

    override val programs: Flow<List<Program>> = programDao.getAllPrograms().map { entities ->
        entities.map { it.toDomain() }
    }

    override suspend fun refreshPrograms() {
        try {
            val dtos = apiService.getPrograms()
            val entities = dtos.map { it.toEntity() }
            programDao.insertPrograms(entities)
        } catch (e: Exception) {
            // In a real app we'd log or handle the network error,
            // but the offline-first flow continues to emit cached data from Room.
            e.printStackTrace()
        }
    }
}
