package com.example.degreewiki.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedItemDao {
    @Query("SELECT * FROM saved_items WHERE ownerUserId = :ownerUserId ORDER BY savedAt DESC")
    fun observeSavedPrograms(ownerUserId: String): Flow<List<SavedItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedProgram(item: SavedItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedPrograms(items: List<SavedItemEntity>)

    @Query("DELETE FROM saved_items WHERE ownerUserId = :ownerUserId")
    suspend fun deleteForUser(ownerUserId: String)

    @Query(
        "DELETE FROM saved_items WHERE ownerUserId = :ownerUserId AND savedItemId = :savedItemId"
    )
    suspend fun deleteSavedItem(ownerUserId: String, savedItemId: String)

    @Transaction
    suspend fun replaceForUser(ownerUserId: String, items: List<SavedItemEntity>) {
        deleteForUser(ownerUserId)
        insertSavedPrograms(items)
    }
}
