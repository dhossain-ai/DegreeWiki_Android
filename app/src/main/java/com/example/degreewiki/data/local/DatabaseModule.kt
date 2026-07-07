package com.example.degreewiki.data.local

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): DegreeWikiDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            DegreeWikiDatabase::class.java,
            "degreewiki.db"
        )
        .fallbackToDestructiveMigration(true)
        .build()
    }

    @Provides
    @Singleton
    fun provideSavedItemDao(database: DegreeWikiDatabase): SavedItemDao {
        return database.savedItemDao()
    }

    @Provides
    @Singleton
    fun provideCountryDao(database: DegreeWikiDatabase) = database.countryDao()

    @Provides
    @Singleton
    fun provideUniversityDao(database: DegreeWikiDatabase) = database.universityDao()

    @Provides
    @Singleton
    fun provideProgramDao(database: DegreeWikiDatabase) = database.programDao()
}
