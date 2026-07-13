package com.example.degreewiki.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

import com.example.degreewiki.data.local.dao.CountryDao
import com.example.degreewiki.data.local.dao.ProgramDao
import com.example.degreewiki.data.local.dao.UniversityDao
import com.example.degreewiki.data.local.dao.ScholarshipDao
import com.example.degreewiki.data.local.dao.GuideDao
import com.example.degreewiki.data.local.entity.CountryEntity
import com.example.degreewiki.data.local.entity.ProgramEntity
import com.example.degreewiki.data.local.entity.UniversityEntity
import com.example.degreewiki.data.local.entity.ScholarshipEntity
import com.example.degreewiki.data.local.entity.GuideEntity

@Database(
    entities = [
        SavedItemEntity::class,
        CountryEntity::class,
        UniversityEntity::class,
        ProgramEntity::class,
        ScholarshipEntity::class,
        GuideEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class DegreeWikiDatabase : RoomDatabase() {
    abstract fun savedItemDao(): SavedItemDao
    abstract fun countryDao(): CountryDao
    abstract fun universityDao(): UniversityDao
    abstract fun programDao(): ProgramDao
    abstract fun scholarshipDao(): ScholarshipDao
    abstract fun guideDao(): GuideDao
}
