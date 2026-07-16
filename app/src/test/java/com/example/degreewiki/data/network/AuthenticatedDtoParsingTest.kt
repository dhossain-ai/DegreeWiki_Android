package com.example.degreewiki.data.network

import com.example.degreewiki.data.network.dto.SavedItemsResponseDto
import com.example.degreewiki.data.network.dto.UserProfileResponseDto
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthenticatedDtoParsingTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun profileParsesWithProfile() {
        val response = json.decodeFromString<UserProfileResponseDto>(
            """{"ok":true,"user":{"id":"u1","email":"student@example.com","displayName":null,"createdAt":"2026-01-01T00:00:00Z"},"profile":{"displayName":"Student","avatarUrl":null,"accountStatus":"active"},"savedSummary":{"programCount":2}}"""
        )

        assertEquals("Student", response.profile?.displayName)
        assertEquals(2, response.savedSummary.programCount)
    }

    @Test
    fun profileParsesWhenProfileIsNull() {
        val response = json.decodeFromString<UserProfileResponseDto>(
            """{"ok":true,"user":{"id":"u1","email":"student@example.com","displayName":null,"createdAt":null},"profile":null,"savedSummary":{"programCount":0}}"""
        )

        assertNull(response.profile)
        assertEquals("student@example.com", response.user.email)
    }

    @Test
    fun savedProgramsResponseParsesProgramAndSavedItemIds() {
        val response = json.decodeFromString<SavedItemsResponseDto>(
            """{"ok":true,"items":[{"savedItemId":"saved-1","entityType":"program","entityId":"program-1","savedAt":"2026-07-16T10:00:00Z","program":{"id":"program-1","slug":"computer-science","title":"Computer Science","universityName":"Example University","countryName":"Romania","degreeLevel":"Bachelor","subject":"Computer Science","tuitionDisplay":"€5,000/year","durationMonths":36,"duration":"3 years"}}]}"""
        )

        assertEquals("saved-1", response.items.single().savedItemId)
        assertEquals("program-1", response.items.single().entityId)
        assertEquals("3 years", response.items.single().program.duration)
    }
}
