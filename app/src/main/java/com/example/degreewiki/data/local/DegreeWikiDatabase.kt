package com.example.degreewiki.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

import com.example.degreewiki.data.local.dao.CountryDao
import com.example.degreewiki.data.local.dao.ProgramDao
import com.example.degreewiki.data.local.dao.UniversityDao
import com.example.degreewiki.data.local.entity.CountryEntity
import com.example.degreewiki.data.local.entity.ProgramEntity
import com.example.degreewiki.data.local.entity.UniversityEntity

@Database(
    entities = [
        SavedItemEntity::class,
        CountryEntity::class,
        UniversityEntity::class,
        ProgramEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class DegreeWikiDatabase : RoomDatabase() {
    abstract fun savedItemDao(): SavedItemDao
    abstract fun countryDao(): CountryDao
    abstract fun universityDao(): UniversityDao
    abstract fun programDao(): ProgramDao
}
