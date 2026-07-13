package com.example.degreewiki.ui.features.discover

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class FilterChoice(val section: String, val value: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramFilterBottomSheet(
    current: ProgramFilters,
    options: ProgramFilterOptions,
    onApply: (ProgramFilters) -> Unit,
    onDismiss: () -> Unit
) {
    var draft by remember(current) { mutableStateOf(current) }
    val choices = buildList {
        options.degreeLevels.forEach { add(FilterChoice("Degree level", it)) }
        options.countries.forEach { add(FilterChoice("Country", it)) }
        options.subjects.forEach { add(FilterChoice("Subject", it)) }
        if (options.universities.size <= 12) options.universities.forEach { add(FilterChoice("University", it)) }
    }
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Filter programs", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                TextButton(onClick = { draft = ProgramFilters() }) { Text("Clear all") }
            }
            LazyColumn(modifier = Modifier.heightIn(max = 520.dp)) {
                choices.groupBy { it.section }.forEach { (section, sectionChoices) ->
                    item(section) {
                        if (section != "Degree level") HorizontalDivider()
                        Text(section, modifier = Modifier.padding(top = 14.dp, bottom = 6.dp), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    }
                    items(sectionChoices, key = { "${it.section}:${it.value}" }) { choice ->
                        val selected = draft.contains(choice)
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable { draft = draft.toggle(choice) }.padding(vertical = 4.dp).semantics {
                                contentDescription = "${choice.value}, ${if (selected) "selected" else "not selected"}"
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(checked = selected, onCheckedChange = { draft = draft.toggle(choice) })
                            Text(choice.value)
                        }
                    }
                }
            }
            Button(
                onClick = { onApply(draft) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Apply${if (draft.count > 0) " (${draft.count})" else ""}") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramSortBottomSheet(
    current: ProgramSortOption,
    onSelect: (ProgramSortOption) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 24.dp)) {
            Text("Sort programs", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            ProgramSortOption.entries.forEach { option ->
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { onSelect(option) }.padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = current == option, onClick = { onSelect(option) })
                    Text(option.label)
                }
            }
        }
    }
}

private fun ProgramFilters.contains(choice: FilterChoice): Boolean = when (choice.section) {
    "Degree level" -> choice.value in degreeLevels
    "Country" -> choice.value in countries
    "Subject" -> choice.value in subjects
    else -> choice.value in universities
}

private fun ProgramFilters.toggle(choice: FilterChoice): ProgramFilters = when (choice.section) {
    "Degree level" -> copy(degreeLevels = degreeLevels.toggle(choice.value))
    "Country" -> copy(countries = countries.toggle(choice.value))
    "Subject" -> copy(subjects = subjects.toggle(choice.value))
    else -> copy(universities = universities.toggle(choice.value))
}

private fun Set<String>.toggle(value: String): Set<String> = if (value in this) this - value else this + value
