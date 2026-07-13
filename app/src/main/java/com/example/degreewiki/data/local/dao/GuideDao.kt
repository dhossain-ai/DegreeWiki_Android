package com.example.degreewiki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.degreewiki.data.local.entity.GuideEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GuideDao {
    @Query("SELECT * FROM guides ORDER BY publishedAt IS NULL, publishedAt DESC, title")
    fun observeAll(): Flow<List<GuideEntity>>

    @Query("SELECT * FROM guides WHERE slug = :slug LIMIT 1")
    fun observeBySlug(slug: String): Flow<GuideEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<GuideEntity>)

    @Query("DELETE FROM guides")
    suspend fun clearAll()

    @Transaction
    suspend fun replaceAll(items: List<GuideEntity>) {
        clearAll()
        insertAll(items)
    }
}
