package com.example.degreewiki.data.mapper

import com.example.degreewiki.data.local.entity.CountryEntity
import com.example.degreewiki.data.local.entity.ProgramEntity
import com.example.degreewiki.data.local.entity.UniversityEntity
import com.example.degreewiki.data.network.dto.CountryDto
import com.example.degreewiki.data.network.dto.ProgramDto
import com.example.degreewiki.data.network.dto.UniversityDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MapperTest {

    @Test
    fun programDto_toEntity_preservesFieldsAndSetsOfflineTimestamp() {
        val dto = ProgramDto(
            id = "program-1",
            slug = "computer-science-bsc",
            title = "Computer Science BSc",
            universityName = "DegreeWiki University",
            countryName = "Romania",
            degreeLevel = "Bachelor",
            subject = "Computer Science",
            tuition = 4200.0,
            duration = "3 years"
        )

        val entity = dto.toEntity()

        assertEquals(dto.id, entity.id)
        assertEquals(dto.slug, entity.slug)
        assertEquals(dto.title, entity.title)
        assertEquals(dto.universityName, entity.universityName)
        assertEquals(dto.countryName, entity.countryName)
        assertEquals(dto.degreeLevel, entity.degreeLevel)
        assertEquals(dto.subject, entity.subject)
        assertEquals(dto.tuition, entity.tuition)
        assertEquals(dto.duration, entity.duration)
        assertTrue(entity.offlineSavedAt > 0)
    }

    @Test
    fun programEntity_toDomain_preservesUserVisibleFields() {
        val entity = ProgramEntity(
            id = "program-1",
            slug = "computer-science-bsc",
            title = "Computer Science BSc",
            universityName = "DegreeWiki University",
            countryName = "Romania",
            degreeLevel = "Bachelor",
            subject = "Computer Science",
            tuition = 4200.0,
            duration = "3 years",
            offlineSavedAt = 1234L
        )

        val domain = entity.toDomain()

        assertEquals(entity.id, domain.id)
        assertEquals(entity.slug, domain.slug)
        assertEquals(entity.title, domain.title)
        assertEquals(entity.universityName, domain.universityName)
        assertEquals(entity.countryName, domain.countryName)
        assertEquals(entity.degreeLevel, domain.degreeLevel)
        assertEquals(entity.subject, domain.subject)
        assertEquals(entity.tuition, domain.tuition)
        assertEquals(entity.duration, domain.duration)
    }

    @Test
    fun universityDto_toEntity_preservesFieldsAndSetsOfflineTimestamp() {
        val dto = UniversityDto(
            id = "university-1",
            slug = "degreewiki-university",
            name = "DegreeWiki University",
            countryId = "country-1",
            city = "Bucharest",
            logoUrl = "https://example.com/logo.png",
            overview = "A verified overview"
        )

        val entity = dto.toEntity()

        assertEquals(dto.id, entity.id)
        assertEquals(dto.slug, entity.slug)
        assertEquals(dto.name, entity.name)
        assertEquals(dto.countryId, entity.countryId)
        assertEquals(dto.city, entity.city)
        assertEquals(dto.logoUrl, entity.logoUrl)
        assertEquals(dto.overview, entity.overview)
        assertTrue(entity.offlineSavedAt > 0)
    }

    @Test
    fun universityEntity_toDomain_preservesUserVisibleFields() {
        val entity = UniversityEntity(
            id = "university-1",
            slug = "degreewiki-university",
            name = "DegreeWiki University",
            countryId = "country-1",
            city = "Bucharest",
            logoUrl = "https://example.com/logo.png",
            overview = "A verified overview",
            offlineSavedAt = 1234L
        )

        val domain = entity.toDomain()

        assertEquals(entity.id, domain.id)
        assertEquals(entity.slug, domain.slug)
        assertEquals(entity.name, domain.name)
        assertEquals(entity.countryId, domain.countryId)
        assertEquals(entity.city, domain.city)
        assertEquals(entity.logoUrl, domain.logoUrl)
        assertEquals(entity.overview, domain.overview)
    }

    @Test
    fun countryDto_toEntity_preservesFieldsAndSetsOfflineTimestamp() {
        val dto = CountryDto(
            id = "country-1",
            slug = "romania",
            name = "Romania",
            summary = "Study destination summary",
            imageUrl = "https://example.com/romania.jpg"
        )

        val entity = dto.toEntity()

        assertEquals(dto.id, entity.id)
        assertEquals(dto.slug, entity.slug)
        assertEquals(dto.name, entity.name)
        assertEquals(dto.summary, entity.summary)
        assertEquals(dto.imageUrl, entity.imageUrl)
        assertTrue(entity.offlineSavedAt > 0)
    }

    @Test
    fun countryEntity_toDomain_preservesUserVisibleFields() {
        val entity = CountryEntity(
            id = "country-1",
            slug = "romania",
            name = "Romania",
            summary = "Study destination summary",
            imageUrl = "https://example.com/romania.jpg",
            offlineSavedAt = 1234L
        )

        val domain = entity.toDomain()

        assertEquals(entity.id, domain.id)
        assertEquals(entity.slug, domain.slug)
        assertEquals(entity.name, domain.name)
        assertEquals(entity.summary, domain.summary)
        assertEquals(entity.imageUrl, domain.imageUrl)
    }
}
