package com.example.degreewiki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.degreewiki.data.local.entity.ScholarshipEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScholarshipDao {
    @Query("SELECT * FROM scholarships ORDER BY deadline IS NULL, deadline, title")
    fun observeAll(): Flow<List<ScholarshipEntity>>

    @Query("SELECT * FROM scholarships WHERE slug = :slug LIMIT 1")
    fun observeBySlug(slug: String): Flow<ScholarshipEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ScholarshipEntity>)

    @Query("DELETE FROM scholarships")
    suspend fun clearAll()

    @Transaction
    suspend fun replaceAll(items: List<ScholarshipEntity>) {
        clearAll()
        insertAll(items)
    }
}
