package com.example.degreewiki.ui.features.scholarships

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ScholarshipDeadlineTest {
    private val today = LocalDate.of(2026, 7, 14)

    @Test fun pastDeadlineIsCautiouslyLabelled() {
        val result = scholarshipDeadlinePresentation("2026-03-01", null, null, today)!!
        assertTrue(result.hasPassed == true)
        assertEquals("Deadline passed · Mar 1, 2026", result.label)
    }

    @Test fun futureDeadlineUsesRealDate() {
        val result = scholarshipDeadlinePresentation("2027-03-31", null, null, today)!!
        assertFalse(result.hasPassed == true)
        assertEquals("Deadline Mar 31, 2027", result.label)
    }

    @Test fun missingDeadlineIsOmitted() {
        assertNull(scholarshipDeadlinePresentation(null, null, null, today))
    }

    @Test fun malformedDeadlineFallsBackWithoutInventingStatus() {
        val result = scholarshipDeadlinePresentation("not-a-date", "Check official deadline", null, today)!!
        assertNull(result.hasPassed)
        assertEquals("Check official deadline", result.label)
    }
}
