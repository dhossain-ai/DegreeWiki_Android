package com.example.degreewiki.ui.features.scholarships

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

data class ScholarshipDeadlinePresentation(val label: String, val hasPassed: Boolean?)

fun scholarshipDeadlinePresentation(
    deadline: String?,
    deadlineDisplay: String?,
    deadlineText: String?,
    today: LocalDate = LocalDate.now()
): ScholarshipDeadlinePresentation? {
    val parsed = try {
        deadline?.trim()?.takeIf(String::isNotEmpty)?.let(LocalDate::parse)
    } catch (_: DateTimeParseException) {
        null
    }
    if (parsed != null) {
        val formatted = parsed.format(DateTimeFormatter.ofPattern("MMM d, uuuu", Locale.US))
        return if (parsed.isBefore(today)) {
            ScholarshipDeadlinePresentation("Deadline passed · $formatted", true)
        } else {
            ScholarshipDeadlinePresentation("Deadline $formatted", false)
        }
    }
    return deadlineDisplay?.trim()?.takeIf(String::isNotEmpty)?.let { ScholarshipDeadlinePresentation(it, null) }
        ?: deadlineText?.trim()?.takeIf(String::isNotEmpty)?.let { ScholarshipDeadlinePresentation(it, null) }
}
