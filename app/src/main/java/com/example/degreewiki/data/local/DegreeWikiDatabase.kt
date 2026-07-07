package com.example.degreewiki.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SavedItemEntity::class], version = 1, exportSchema = false)
abstract class DegreeWikiDatabase : RoomDatabase() {
    abstract fun savedItemDao(): SavedItemDao
}
