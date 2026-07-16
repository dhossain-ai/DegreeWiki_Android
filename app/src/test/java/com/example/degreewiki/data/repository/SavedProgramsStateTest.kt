package com.example.degreewiki.data.repository

import com.example.degreewiki.domain.model.SavedProgram
import com.example.degreewiki.domain.model.distinctByProgramId
import com.example.degreewiki.domain.model.savedItemIdByProgramId
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class SavedProgramsStateTest {
    private val saved = SavedProgram(
        savedItemId = "saved-1",
        programId = "program-1",
        slug = "program",
        title = "Program",
        universityName = null,
        countryName = null,
        degreeLevel = null,
        subject = null,
        tuitionDisplay = null,
        durationMonths = null,
        duration = null,
        savedAt = "2026-07-16T10:00:00Z"
    )

    @Test
    fun mapsProgramIdToSavedItemId() {
        assertEquals("saved-1", listOf(saved).savedItemIdByProgramId()["program-1"])
    }

    @Test
    fun duplicateProgramSaveStateIsCollapsed() {
        assertEquals(1, listOf(saved, saved.copy(savedItemId = "saved-2")).distinctByProgramId().size)
    }

    @Test
    fun logoutClearsSavedAndPendingState() {
        val state = SavedProgramsState(
            items = listOf(saved),
            savedItemIdsByProgramId = mapOf("program-1" to "saved-1"),
            pendingProgramIds = setOf("program-1")
        ).clearedForLogout()

        assertTrue(state.items.isEmpty())
        assertTrue(state.savedItemIdsByProgramId.isEmpty())
        assertTrue(state.pendingProgramIds.isEmpty())
    }

    @Test
    fun only401IsClassifiedAsSessionExpiry() {
        val unauthorized = HttpException(Response.error<Unit>(401, "".toResponseBody()))
        val serverError = HttpException(Response.error<Unit>(500, "".toResponseBody()))

        assertTrue(unauthorized.isUnauthorized())
        assertFalse(serverError.isUnauthorized())
    }
}
