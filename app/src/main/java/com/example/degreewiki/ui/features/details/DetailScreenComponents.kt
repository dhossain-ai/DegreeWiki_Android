package com.example.degreewiki.ui.features.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.degreewiki.ui.components.DegreeWikiCard
import com.example.degreewiki.ui.components.EmptyState
import com.example.degreewiki.ui.components.ImageOrInitials
import com.example.degreewiki.ui.components.PrimaryActionButton
import com.example.degreewiki.ui.components.StatusBadge
import com.example.degreewiki.ui.components.StatusBadgeTone
import com.example.degreewiki.ui.components.TrustNote

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailTopBar(title: String, onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Navigate back")
            }
        }
    )
}

@Composable
internal fun DetailHero(
    title: String,
    subtitle: String? = null,
    location: String? = null,
    badges: List<String> = emptyList(),
    showInitials: Boolean = false,
    summary: String? = null
) {
    DegreeWikiCard {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            if (showInitials) ImageOrInitials(name = title, imageUrl = null)
            if (badges.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    badges.take(3).forEach { StatusBadge(it, StatusBadgeTone.Neutral) }
                }
            }
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            subtitle?.takeIf(String::isNotBlank)?.let {
                Text(it, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            }
            location?.takeIf(String::isNotBlank)?.let {
                Text(it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            summary?.takeIf(String::isNotBlank)?.let {
                Text(
                    text = it,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
internal fun DetailActionRow(
    actions: List<Pair<String, String>>,
    modifier: Modifier = Modifier
) {
    val safeActions = actions.mapNotNull { (label, url) -> safeExternalUrl(url)?.let { label to it } }
    if (safeActions.isEmpty()) return

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        safeActions.forEachIndexed { index, (label, url) ->
            ExternalLinkButton(label = label, url = url, primary = index == 0)
        }
    }
}

@Composable
internal fun ExternalLinkButton(
    label: String,
    url: String?,
    modifier: Modifier = Modifier,
    primary: Boolean = false,
    onOpen: ((String) -> Unit)? = null
) {
    val safeUrl = safeExternalUrl(url) ?: return
    val uriHandler = LocalUriHandler.current
    val open = {
        runCatching { onOpen?.invoke(safeUrl) ?: uriHandler.openUri(safeUrl) }
        Unit
    }
    val content: @Composable () -> Unit = {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(label)
            Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(18.dp))
        }
    }
    if (primary) {
        Button(onClick = open, modifier = modifier.fillMaxWidth(), content = { content() })
    } else {
        OutlinedButton(onClick = open, modifier = modifier.fillMaxWidth(), content = { content() })
    }
}

internal fun safeExternalUrl(url: String?): String? {
    val trimmed = url?.trim()?.takeIf(String::isNotBlank) ?: return null
    return runCatching {
        val uri = trimmed.toUri()
        trimmed.takeIf { uri.scheme.equals("http", true) || uri.scheme.equals("https", true) }
    }.getOrNull()
}

@Composable
internal fun KeyFactsGrid(facts: List<Pair<String, String>>, modifier: Modifier = Modifier) {
    val visibleFacts = facts.filter { it.first.isNotBlank() && it.second.isNotBlank() }
    if (visibleFacts.isEmpty()) return

    DetailSection(title = "Key facts", modifier = modifier) {
        visibleFacts.chunked(2).forEachIndexed { rowIndex, rowFacts ->
            if (rowIndex > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                rowFacts.forEach { (label, value) ->
                    KeyFactItem(label = label, value = value, modifier = Modifier.weight(1f))
                }
                if (rowFacts.size == 1) androidx.compose.foundation.layout.Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
internal fun KeyFactItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
internal fun DetailSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            content()
        }
    }
}

@Composable
internal fun DetailTextSection(title: String, body: String) {
    if (body.isBlank()) return
    DetailSection(title = title) {
        Text(body, style = MaterialTheme.typography.bodyMedium, lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.15f, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
internal fun ExpandableTextSection(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    collapsedLines: Int = 6,
    longTextThreshold: Int = 320
) {
    if (body.isBlank()) return
    var expanded by remember(body) { mutableStateOf(false) }
    val expandable = body.length > longTextThreshold || body.count { it == '\n' } >= collapsedLines
    DetailSection(title = title, modifier = modifier) {
        Text(
            text = body,
            maxLines = if (expandable && !expanded) collapsedLines else Int.MAX_VALUE,
            overflow = if (expandable && !expanded) TextOverflow.Ellipsis else TextOverflow.Clip,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.15f,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (expandable) {
            TextButton(onClick = { expanded = !expanded }) {
                Text(if (expanded) "Show less" else "Read more")
            }
        }
    }
}

@Composable
internal fun RelatedContentRow(
    title: String,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            subtitle?.takeIf(String::isNotBlank)?.let {
                Text(it, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        if (onClick != null) Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Open", tint = MaterialTheme.colorScheme.primary)
    }
}

@Composable
internal fun SourceStatusSection(status: String?, lastChecked: String?) {
    val normalized = status?.trim()?.lowercase().orEmpty()
    val verified = normalized == "verified" || normalized == "fully verified"
    val label = when {
        verified -> "Verified"
        normalized.contains("partial") -> "Partially verified"
        else -> "Not independently verified"
    }
    val message = if (verified) {
        "Information checked against public sources."
    } else {
        "Confirm tuition, deadlines, and admission requirements on the official source."
    }
    DetailSection(title = "Source and verification") {
        StatusBadge(label, if (verified) StatusBadgeTone.Verified else StatusBadgeTone.Deadline)
        Text(message, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        lastChecked?.takeIf(String::isNotBlank)?.let {
            Text("Last checked: $it", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
internal fun FaqAccordionItem(question: String, answer: String, modifier: Modifier = Modifier) {
    var expanded by remember(question) { mutableStateOf(false) }
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded }.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(question, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = if (expanded) "Collapse answer" else "Expand answer")
            }
            if (expanded) {
                HorizontalDivider()
                Text(answer, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
internal fun DetailUnavailableState(
    title: String,
    message: String,
    actionLabel: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EmptyState(title = title, message = message, modifier = modifier, actionLabel = actionLabel, onActionClick = onActionClick)
}

// Compatibility adapters retained only for the shared-component commit boundary.
@Composable internal fun DetailTopAppBar(title: String, onBackClick: () -> Unit) = DetailTopBar(title, onBackClick)
@Composable internal fun DetailHeroCard(title: String, subtitle: String? = null, badge: String? = null, supportingLines: List<String> = emptyList()) =
    DetailHero(title = title, subtitle = subtitle, badges = listOfNotNull(badge), location = supportingLines.firstOrNull())
@Composable internal fun DetailFactsCard(title: String, subtitle: String? = null, facts: List<Pair<String, String>>) {
    if (facts.isNotEmpty()) DetailSection(title) { subtitle?.let { Text(it) }; facts.forEach { KeyFactItem(it.first, it.second) } }
}
@Composable internal fun RelatedTextListCard(title: String, subtitle: String? = null, items: List<String>) {
    if (items.isNotEmpty()) DetailSection(title) { subtitle?.let { Text(it) }; items.forEach { RelatedContentRow(it) } }
}
@Composable internal fun DetailTrustNote(text: String) = TrustNote(text)
@Composable internal fun DetailFooterAction(text: String, onClick: () -> Unit) = PrimaryActionButton(text, onClick)
