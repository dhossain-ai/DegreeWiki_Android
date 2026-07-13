package com.example.degreewiki.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """CREATE TABLE IF NOT EXISTS `scholarships` (
                    `id` TEXT NOT NULL, `slug` TEXT NOT NULL, `title` TEXT NOT NULL,
                    `providerName` TEXT, `summary` TEXT, `scholarshipTypeLabel` TEXT,
                    `fundingTypeLabel` TEXT, `amountDisplay` TEXT, `deadline` TEXT,
                    `deadlineText` TEXT, `deadlineDisplay` TEXT,
                    `studyCountriesJson` TEXT NOT NULL, `degreeLevelsJson` TEXT NOT NULL,
                    `subjectsJson` TEXT NOT NULL, `officialUrl` TEXT, `applicationUrl` TEXT,
                    `verificationStatus` TEXT, `lastVerifiedAt` TEXT, `imageUrl` TEXT,
                    `offlineSavedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))""".trimIndent()
            )
            db.execSQL(
                """CREATE TABLE IF NOT EXISTS `guides` (
                    `id` TEXT NOT NULL, `slug` TEXT NOT NULL, `title` TEXT NOT NULL,
                    `summary` TEXT, `categoryId` TEXT, `categorySlug` TEXT, `categoryName` TEXT,
                    `countriesJson` TEXT NOT NULL, `subjectsJson` TEXT NOT NULL,
                    `degreeLevelsJson` TEXT NOT NULL, `publishedAt` TEXT, `updatedAt` TEXT,
                    `coverImageUrl` TEXT, `offlineSavedAt` INTEGER NOT NULL,
                    PRIMARY KEY(`id`))""".trimIndent()
            )
        }
    }

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
        .addMigrations(MIGRATION_2_3)
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

    @Provides
    @Singleton
    fun provideScholarshipDao(database: DegreeWikiDatabase) = database.scholarshipDao()

    @Provides
    @Singleton
    fun provideGuideDao(database: DegreeWikiDatabase) = database.guideDao()
}
