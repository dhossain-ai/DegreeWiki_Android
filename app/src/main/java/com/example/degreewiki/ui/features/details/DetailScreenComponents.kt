package com.example.degreewiki.ui.features.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.degreewiki.ui.components.DegreeWikiCard
import com.example.degreewiki.ui.components.EmptyState
import com.example.degreewiki.ui.components.PrimaryActionButton
import com.example.degreewiki.ui.components.SectionHeader
import com.example.degreewiki.ui.components.StatusBadge
import com.example.degreewiki.ui.components.StatusBadgeTone
import com.example.degreewiki.ui.components.TrustNote

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailTopAppBar(
    title: String,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigate back"
                )
            }
        }
    )
}

@Composable
internal fun DetailHeroCard(
    title: String,
    subtitle: String? = null,
    badge: String? = null,
    supportingLines: List<String> = emptyList()
) {
    DegreeWikiCard {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            badge?.let {
                StatusBadge(
                    label = it,
                    tone = StatusBadgeTone.Neutral
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            supportingLines.forEach { line ->
                Text(
                    text = line,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
internal fun DetailFactsCard(
    title: String,
    subtitle: String? = null,
    facts: List<Pair<String, String>>
) {
    if (facts.isEmpty()) return

    DegreeWikiCard {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SectionHeader(
                title = title,
                subtitle = subtitle
            )
            facts.forEachIndexed { index, (label, value) ->
                if (index > 0) {
                    HorizontalDivider()
                }
                DetailRow(
                    label = label,
                    value = value
                )
            }
        }
    }
}

@Composable
internal fun DetailTextSection(
    title: String,
    body: String
) {
    DegreeWikiCard {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
internal fun RelatedTextListCard(
    title: String,
    subtitle: String? = null,
    items: List<String>
) {
    if (items.isEmpty()) return

    DegreeWikiCard {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SectionHeader(
                title = title,
                subtitle = subtitle
            )
            items.forEachIndexed { index, item ->
                if (index > 0) {
                    HorizontalDivider()
                }
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
internal fun DetailTrustNote(text: String) {
    TrustNote(text = text)
}

@Composable
internal fun DetailUnavailableState(
    title: String,
    message: String,
    actionLabel: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EmptyState(
        title = title,
        message = message,
        modifier = modifier,
        actionLabel = actionLabel,
        onActionClick = onActionClick
    )
}

@Composable
internal fun DetailFooterAction(
    text: String,
    onClick: () -> Unit
) {
    PrimaryActionButton(
        text = text,
        onClick = onClick
    )
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )
    }
}
