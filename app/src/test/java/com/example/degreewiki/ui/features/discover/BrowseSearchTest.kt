package com.example.degreewiki.ui.features.discover

import com.example.degreewiki.domain.model.Country
import com.example.degreewiki.domain.model.Program
import com.example.degreewiki.domain.model.University
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BrowseSearchTest {
    private val programs = listOf(
        Program("1", "cs", "Computer Science", "Aalto University", "Finland", "Master's", "Computer Science", null, "2 years"),
        Program("2", "business", "Business Analytics", "Berlin School", "Germany", "Bachelor's", "Business", null, null),
        Program("3", "design", "Digital Design", "Aalto University", "Finland", "Bachelor's", null, null, null)
    )

    @Test fun programSearch_isCaseInsensitiveAndMatchesPartialWords() {
        assertEquals(listOf("1"), filterPrograms(programs, ProgramSearchState(query = "  comp SCI ")).map { it.id })
    }

    @Test fun programSearch_matchesUniversityCountrySubjectAndDegree() {
        assertEquals(2, filterPrograms(programs, ProgramSearchState(query = "aalto")).size)
        assertEquals(2, filterPrograms(programs, ProgramSearchState(query = "finland")).size)
        assertEquals(listOf("2"), filterPrograms(programs, ProgramSearchState(query = "business")).map { it.id })
        assertEquals(2, filterPrograms(programs, ProgramSearchState(query = "bachelor")).size)
    }

    @Test fun filtersCombineAcrossDimensionsAndAllowMultipleWithinDimension() {
        val state = ProgramSearchState(
            filters = ProgramFilters(
                degreeLevels = setOf("Master's", "Bachelor's"),
                countries = setOf("Finland"),
                universities = setOf("Aalto University")
            )
        )
        assertEquals(listOf("1", "3"), filterPrograms(programs, state).map { it.id })
        assertEquals(listOf("1"), filterPrograms(programs, state.copy(filters = state.filters.copy(degreeLevels = setOf("Master's")))).map { it.id })
    }

    @Test fun removingOneFilterAndClearAllRestoreExpectedResults() {
        val filters = ProgramFilters(countries = setOf("Finland"), subjects = setOf("Computer Science"))
        assertEquals(1, filterPrograms(programs, ProgramSearchState(filters = filters)).size)
        assertEquals(2, filterPrograms(programs, ProgramSearchState(filters = filters.remove("Computer Science"))).size)
        assertEquals(3, filterPrograms(programs, ProgramSearchState()).size)
    }

    @Test fun sortingUsesOnlyReliableTextFields() {
        assertEquals(listOf("Business Analytics", "Computer Science", "Digital Design"), filterPrograms(programs, ProgramSearchState(sort = ProgramSortOption.TITLE_ASC)).map { it.title })
        assertEquals(listOf("Digital Design", "Computer Science", "Business Analytics"), filterPrograms(programs, ProgramSearchState(sort = ProgramSortOption.TITLE_DESC)).map { it.title })
        assertEquals(listOf("Aalto University", "Aalto University", "Berlin School"), filterPrograms(programs, ProgramSearchState(sort = ProgramSortOption.UNIVERSITY_ASC)).map { it.universityName })
    }

    @Test fun missingNullableFieldsDoNotCrashSearchOrFilters() {
        assertTrue(filterPrograms(programs, ProgramSearchState(query = "missing")).isEmpty())
        assertTrue(filterPrograms(programs, ProgramSearchState(filters = ProgramFilters(subjects = setOf("Unknown")))).isEmpty())
    }

    @Test fun universityAndCountrySearchUseOnlyAvailableFields() {
        val universities = listOf(
            University("u1", "aalto", "Aalto University", "c1", "Espoo", null, null),
            University("u2", "berlin", "Berlin School", "c2", null, null, "Business education")
        )
        val countries = listOf(
            Country("c1", "finland", "Finland", "Nordic study destination", null),
            Country("c2", "germany", "Germany", null, null)
        )
        assertEquals(listOf("u1"), filterUniversities(universities, "espo").map { it.id })
        assertEquals(listOf("u2"), filterUniversities(universities, "business").map { it.id })
        assertEquals(listOf("c1"), filterCountries(countries, "nordic").map { it.id })
        assertEquals(listOf("c2"), filterCountries(countries, "GERM").map { it.id })
    }
}
