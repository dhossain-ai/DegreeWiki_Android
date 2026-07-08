package com.example.degreewiki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.degreewiki.data.local.entity.UniversityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UniversityDao {
    @Query("SELECT * FROM universities")
    fun getAllUniversities(): Flow<List<UniversityEntity>>

    @Query("SELECT * FROM universities WHERE id = :id")
    fun getUniversityById(id: String): Flow<UniversityEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUniversities(universities: List<UniversityEntity>)

    @Query("DELETE FROM universities")
    suspend fun clearAll()
}
