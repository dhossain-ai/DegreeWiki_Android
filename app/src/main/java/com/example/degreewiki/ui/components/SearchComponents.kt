package com.example.degreewiki.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun BrowseSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    onSearch: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth().semantics { contentDescription = placeholder },
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear search")
                }
            }
        },
        singleLine = true,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus(); onSearch() })
    )
}

@Composable
fun BrowseFilterButton(selectedCount: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.semantics { contentDescription = if (selectedCount > 0) "Filters, $selectedCount selected" else "Filters" }
    ) {
        BadgedBox(badge = { if (selectedCount > 0) Badge { Text(selectedCount.toString()) } }) {
            Icon(Icons.Default.FilterList, contentDescription = null)
        }
        Text("Filters")
    }
}

@Composable
fun BrowseSortButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(onClick = onClick, modifier = modifier.semantics { contentDescription = "Sort programs, $label" }) {
        Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = null)
        Text(label)
    }
}

@Composable
fun ActiveFilterChips(
    filters: List<String>,
    onRemove: (String) -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (filters.isEmpty()) return
    LazyRow(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(filters, key = { it }) { filter ->
            InputChip(
                selected = true,
                onClick = { onRemove(filter) },
                label = { Text(filter) },
                trailingIcon = { Icon(Icons.Default.Clear, contentDescription = "Remove $filter filter") }
            )
        }
        if (filters.size > 1) item { TextButton(onClick = onClearAll) { Text("Clear all") } }
    }
}

@Composable
fun BrowseResultsHeader(count: Int, totalCount: Int, hasSearch: Boolean, noun: String, modifier: Modifier = Modifier) {
    val label = when {
        hasSearch -> "$count ${if (count == 1) noun else "${noun}s"} found"
        else -> "$totalCount ${if (totalCount == 1) noun else "${noun}s"}"
    }
    Text(label, modifier = modifier, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
}

@Composable
fun SearchEmptyState(
    title: String,
    message: String,
    hasQuery: Boolean,
    hasFilters: Boolean,
    onClearSearch: () -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    DegreeWikiCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (hasQuery) TextButton(onClick = onClearSearch) { Text("Clear search") }
                if (hasFilters) TextButton(onClick = onClearFilters) { Text("Clear filters") }
            }
        }
    }
}
