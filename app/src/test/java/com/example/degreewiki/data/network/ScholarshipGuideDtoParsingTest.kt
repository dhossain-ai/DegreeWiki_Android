package com.example.degreewiki.data.network

import com.example.degreewiki.data.network.dto.DetailResponse
import com.example.degreewiki.data.network.dto.GuideDetailDto
import com.example.degreewiki.data.network.dto.GuideDto
import com.example.degreewiki.data.network.dto.ScholarshipDetailDto
import com.example.degreewiki.data.network.dto.ScholarshipDto
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ScholarshipGuideDtoParsingTest {
    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

    @Test fun scholarshipRawListAndWrappedDetailParse() {
        val list = json.decodeFromString<List<ScholarshipDto>>("""[{"id":"s1","slug":"award","name":"Award","providerName":"Ministry","amountDisplay":"€5,000","deadline":"2027-03-31","studyCountries":["Romania"],"futureOptionalField":true}]""")
        assertEquals("Award", list.single().name)
        assertEquals(listOf("Romania"), list.single().studyCountries)

        val detail = json.decodeFromString<DetailResponse<ScholarshipDetailDto>>("""{"ok":true,"item":{"id":"s1","slug":"award","name":"Award","contentFormat":"plain_text","amount":{"min":1000,"display":"€1,000"},"deadline":{"date":"2027-03-31"},"eligibleNationalities":[{"country":{"id":"c1","name":"Bulgaria"},"eligibilityType":"eligible"}]}}""")
        assertTrue(detail.ok)
        assertEquals(1000.0, detail.item?.amount?.min ?: 0.0, 0.0)
        assertEquals("Bulgaria", detail.item?.eligibleNationalities?.single()?.country?.name)
    }

    @Test fun guideRawListAndStructuredDetailParseUnknownBlocks() {
        val list = json.decodeFromString<List<GuideDto>>("""[{"id":"g1","slug":"guide","title":"Guide","category":{"id":"cat","slug":"visas","name":"Visas"},"countries":[]}]""")
        assertEquals("Visas", list.single().category?.name)

        val detail = json.decodeFromString<DetailResponse<GuideDetailDto>>("""{"ok":true,"item":{"id":"g1","slug":"guide","title":"Guide","contentFormat":"structured_blocks_v1","body":[{"type":"heading","level":2,"children":[{"type":"text","text":"Start"}]},{"type":"future_block","payload":{"anything":true}}],"relatedGuides":[]}}""")
        assertEquals("heading", detail.item?.body?.first()?.type)
        assertEquals("future_block", detail.item?.body?.last()?.type)
    }

    @Test fun missingOptionalFieldsRemainEmptyOrNull() {
        val scholarship = json.decodeFromString<ScholarshipDto>("""{"id":"s","slug":"s","name":"Scholarship"}""")
        val guide = json.decodeFromString<GuideDto>("""{"id":"g","slug":"g","title":"Guide"}""")
        assertNull(scholarship.deadline)
        assertTrue(scholarship.eligibleSubjects.isEmpty())
        assertNull(guide.category)
        assertTrue(guide.degreeLevels.isEmpty())
    }
}
