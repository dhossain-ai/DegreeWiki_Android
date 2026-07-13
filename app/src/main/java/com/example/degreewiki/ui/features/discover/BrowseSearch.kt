package com.example.degreewiki.ui.features.discover

import com.example.degreewiki.domain.model.Country
import com.example.degreewiki.domain.model.Program
import com.example.degreewiki.domain.model.University

enum class ProgramSortOption(val label: String) {
    DEFAULT("Recommended"),
    TITLE_ASC("Title A–Z"),
    TITLE_DESC("Title Z–A"),
    UNIVERSITY_ASC("University A–Z")
}

data class ProgramFilters(
    val degreeLevels: Set<String> = emptySet(),
    val countries: Set<String> = emptySet(),
    val subjects: Set<String> = emptySet(),
    val universities: Set<String> = emptySet()
) {
    val count: Int get() = degreeLevels.size + countries.size + subjects.size + universities.size
    val isEmpty: Boolean get() = count == 0

    fun remove(value: String): ProgramFilters = copy(
        degreeLevels = degreeLevels - value,
        countries = countries - value,
        subjects = subjects - value,
        universities = universities - value
    )
}

data class ProgramFilterOptions(
    val degreeLevels: List<String> = emptyList(),
    val countries: List<String> = emptyList(),
    val subjects: List<String> = emptyList(),
    val universities: List<String> = emptyList()
)

data class ProgramSearchState(
    val query: String = "",
    val filters: ProgramFilters = ProgramFilters(),
    val sort: ProgramSortOption = ProgramSortOption.DEFAULT
)

internal fun filterPrograms(
    programs: List<Program>,
    state: ProgramSearchState
): List<Program> {
    val query = normalizeQuery(state.query)
    val filtered = programs.filter { program ->
        val searchableFields = listOfNotNull(
            program.title,
            program.universityName,
            program.countryName,
            program.subject,
            program.degreeLevel
        ).map(::normalizeQuery)
        val matchesQuery = query.isBlank() || query.split(' ').all { token ->
            searchableFields.any { it.contains(token) }
        }
        matchesQuery &&
            matchesSelection(program.degreeLevel, state.filters.degreeLevels) &&
            matchesSelection(program.countryName, state.filters.countries) &&
            matchesSelection(program.subject, state.filters.subjects) &&
            matchesSelection(program.universityName, state.filters.universities)
    }
    return when (state.sort) {
        ProgramSortOption.DEFAULT -> filtered
        ProgramSortOption.TITLE_ASC -> filtered.sortedBy { it.title.lowercase() }
        ProgramSortOption.TITLE_DESC -> filtered.sortedByDescending { it.title.lowercase() }
        ProgramSortOption.UNIVERSITY_ASC -> filtered.sortedWith(
            compareBy<Program> { it.universityName.lowercase() }.thenBy { it.title.lowercase() }
        )
    }
}

internal fun programFilterOptions(programs: List<Program>): ProgramFilterOptions = ProgramFilterOptions(
    degreeLevels = programs.map { it.degreeLevel }.cleanOptions(),
    countries = programs.map { it.countryName }.cleanOptions(),
    subjects = programs.mapNotNull { it.subject }.cleanOptions(),
    universities = programs.map { it.universityName }.cleanOptions()
)

internal fun filterUniversities(universities: List<University>, query: String): List<University> {
    val normalized = normalizeQuery(query)
    if (normalized.isBlank()) return universities
    return universities.filter { university ->
        listOfNotNull(university.name, university.city, university.overview)
            .any { normalizeQuery(it).contains(normalized) }
    }
}

internal fun filterCountries(countries: List<Country>, query: String): List<Country> {
    val normalized = normalizeQuery(query)
    if (normalized.isBlank()) return countries
    return countries.filter { country ->
        listOfNotNull(country.name, country.summary)
            .any { normalizeQuery(it).contains(normalized) }
    }
}

internal fun normalizeQuery(value: String): String = value.trim().lowercase().replace(Regex("\\s+"), " ")

private fun matchesSelection(value: String?, selected: Set<String>): Boolean =
    selected.isEmpty() || value?.let(selected::contains) == true

private fun List<String>.cleanOptions(): List<String> =
    map(String::trim).filter(String::isNotBlank).distinctBy(String::lowercase).sortedBy(String::lowercase)
