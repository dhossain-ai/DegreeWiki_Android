package com.example.degreewiki.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedItemDao {
    @Query("SELECT * FROM saved_items ORDER BY savedAt DESC")
    fun getAllSavedItems(): Flow<List<SavedItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedItem(item: SavedItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedItems(items: List<SavedItemEntity>)

    @Query("DELETE FROM saved_items WHERE id = :id")
    suspend fun deleteSavedItemById(id: String)
}
