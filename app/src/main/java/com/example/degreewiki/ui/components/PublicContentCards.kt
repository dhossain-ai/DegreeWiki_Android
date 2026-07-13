package com.example.degreewiki.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.degreewiki.domain.model.Guide
import com.example.degreewiki.domain.model.Scholarship
import com.example.degreewiki.ui.features.scholarships.scholarshipDeadlinePresentation

@Composable
fun ScholarshipBrowseCard(scholarship: Scholarship, onClick: () -> Unit, modifier: Modifier = Modifier) {
    DegreeWikiCard(modifier = modifier, onClick = onClick) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(scholarship.title, maxLines = 2, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                scholarship.providerName?.let { Text(it, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium) }
                scholarship.summary?.let { Text(it, maxLines = 3, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                val labels = listOfNotNull(scholarship.amountDisplay, scholarship.fundingTypeLabel).distinct()
                if (labels.isNotEmpty()) CompactFactRow(labels)
                scholarshipDeadlinePresentation(scholarship.deadline, scholarship.deadlineDisplay, scholarship.deadlineText)?.let {
                    StatusBadge(it.label, if (it.hasPassed == false) StatusBadgeTone.Neutral else StatusBadgeTone.Deadline)
                }
                val scope = listOfNotNull(scholarship.degreeLevels.firstOrNull(), scholarship.studyCountries.firstOrNull())
                if (scope.isNotEmpty()) CompactFactRow(scope)
                VerificationCue(scholarship.verificationStatus)
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "View scholarship", tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun GuideBrowseCard(guide: Guide, onClick: () -> Unit, modifier: Modifier = Modifier) {
    DegreeWikiCard(modifier = modifier, onClick = onClick) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
            ImageOrInitials(guide.title, guide.coverImageUrl)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                guide.category?.name?.let { MetadataBadge(it) }
                Text(guide.title, maxLines = 2, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                guide.summary?.let { Text(it, maxLines = 3, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                val meta = listOfNotNull(formatPublicDate(guide.publishedAt), guide.countries.firstOrNull(), guide.subjects.firstOrNull())
                if (meta.isNotEmpty()) CompactFactRow(meta)
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Read guide", tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun VerificationCue(status: String?) {
    when (status?.trim()?.lowercase()) {
        "verified" -> StatusBadge("Verified", StatusBadgeTone.Verified)
        "partially_verified" -> StatusBadge("Partially verified", StatusBadgeTone.Deadline)
    }
}

fun formatPublicDate(value: String?): String? = runCatching {
    val instant = java.time.OffsetDateTime.parse(value)
    instant.format(java.time.format.DateTimeFormatter.ofPattern("MMM d, uuuu", java.util.Locale.US))
}.getOrNull()
