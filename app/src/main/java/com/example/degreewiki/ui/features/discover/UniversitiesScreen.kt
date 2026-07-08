package com.example.degreewiki.ui.features.discover

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.degreewiki.domain.model.University

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversitiesScreen(
    onItemClick: (String) -> Unit,
    viewModel: UniversitiesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Universities") })
        },
        modifier = modifier
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (state) {
                is DiscoveryUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DiscoveryUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error loading universities",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.refresh() }) {
                            Text("Retry")
                        }
                    }
                }
                is DiscoveryUiState.Success -> {
                    val data = (state as DiscoveryUiState.Success<University>).data
                    if (data.isEmpty()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("No universities found.")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.refresh() }) {
                                Text("Refresh")
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(data, key = { it.id }) { university ->
                                UniversityCard(
                                    university = university,
                                    onClick = { onItemClick(university.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UniversityCard(university: University, onClick: () -> Unit, modifier: Modifier = Modifier) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = university.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            university.city?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            university.overview?.let {
                Text(text = it, style = MaterialTheme.typography.bodySmall, maxLines = 3)
            }
        }
    }
}
