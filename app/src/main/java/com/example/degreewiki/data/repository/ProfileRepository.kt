package com.example.degreewiki.data.repository

import com.example.degreewiki.data.local.SavedItemDao
import com.example.degreewiki.data.local.SavedItemEntity
import com.example.degreewiki.data.network.DegreeWikiApiService
import com.example.degreewiki.domain.model.SavedItem
import com.example.degreewiki.domain.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface ProfileRepository {
    val savedItems: Flow<List<SavedItem>>
    suspend fun fetchProfile(): UserProfile
    suspend fun refreshSavedItems()
    suspend fun removeSavedItem(id: String)
}

@Singleton
class DefaultProfileRepository @Inject constructor(
    private val apiService: DegreeWikiApiService,
    private val savedItemDao: SavedItemDao
) : ProfileRepository {

    override val savedItems: Flow<List<SavedItem>> = savedItemDao.getAllSavedItems().map { entities ->
        entities.map { entity ->
            SavedItem(
                id = entity.id,
                entityType = entity.entityType,
                entityId = entity.entityId,
                title = entity.title,
                slug = entity.slug,
                thumbnailUrl = entity.thumbnailUrl,
                savedAt = entity.savedAt
            )
        }
    }

    override suspend fun fetchProfile(): UserProfile {
        return withContext(Dispatchers.IO) {
            val dto = apiService.getProfile()
            UserProfile(
                id = dto.id,
                email = dto.email,
                name = dto.name,
                role = dto.role
            )
        }
    }

    override suspend fun refreshSavedItems() {
        withContext(Dispatchers.IO) {
            val dtos = apiService.getSavedItems()
            val entities = dtos.map { dto ->
                SavedItemEntity(
                    id = dto.id,
                    entityType = dto.entityType,
                    entityId = dto.entityId,
                    title = dto.title,
                    slug = dto.slug,
                    thumbnailUrl = dto.thumbnail,
                    savedAt = dto.savedAt
                )
            }
            savedItemDao.insertSavedItems(entities)
        }
    }

    override suspend fun removeSavedItem(id: String) {
        withContext(Dispatchers.IO) {
            apiService.deleteSavedItem(id)
            savedItemDao.deleteSavedItemById(id)
        }
    }
}
