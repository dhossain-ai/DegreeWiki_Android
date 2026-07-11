package com.example.degreewiki.data.network

import com.example.degreewiki.data.network.dto.*
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class PublicDtoParsingTest {
    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

    @Test fun expandedProgramListFieldsParse() {
        val dto = json.decodeFromString<ProgramDto>("""{"id":"p","slug":"p","title":"P","universityName":"U","countryName":"C","degreeLevel":"Master","location":"Cluj, Romania","language":"English","tuitionDisplay":"€5,000/year","verificationStatus":"verified"}""")
        assertEquals("Cluj, Romania", dto.location)
        assertEquals("English", dto.language)
        assertEquals("€5,000/year", dto.tuitionDisplay)
        assertEquals("verified", dto.verificationStatus)
    }

    @Test fun oldListPayloadStillParsesWithMissingOptionalFields() {
        val dto = json.decodeFromString<CountryDto>("""{"id":"c","slug":"c","name":"Country"}""")
        assertNull(dto.iso2)
        assertNull(dto.tuitionOverview)
    }

    @Test fun wrappedProgramDetailParsesNestedFields() {
        val response = json.decodeFromString<DetailResponse<ProgramDetailDto>>("""{"ok":true,"item":{"id":"p","slug":"p","title":"Program","university":{"name":"University"},"tuition":{"display":"€5,000/year"},"admissionRequirements":"Portfolio"}}""")
        assertTrue(response.ok)
        assertEquals("University", response.item?.university?.name)
        assertEquals("€5,000/year", response.item?.tuition?.display)
        assertEquals("Portfolio", response.item?.admissionRequirements)
    }
}
