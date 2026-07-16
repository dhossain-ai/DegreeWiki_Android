package com.example.degreewiki.ui.features.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.degreewiki.data.repository.ProfileState
import com.example.degreewiki.ui.components.DegreeWikiCard
import com.example.degreewiki.ui.components.DegreeWikiScreen
import com.example.degreewiki.ui.components.ErrorState
import com.example.degreewiki.ui.components.ScreenHero

@Composable
fun ProfileScreen(
    onLoginClick: () -> Unit,
    onSavedProgramsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val savedState by viewModel.savedProgramsState.collectAsStateWithLifecycle()

    when (val state = profileState) {
        ProfileState.Loading -> Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(80.dp))
            CircularProgressIndicator()
        }
        ProfileState.LoggedOut -> LoggedOutProfileContent(
            onLoginClick = onLoginClick,
            modifier = modifier
        )
        ProfileState.SessionExpired -> LoggedOutProfileContent(
            onLoginClick = onLoginClick,
            message = "Your session has expired. Log in again to continue.",
            modifier = modifier
        )
        is ProfileState.Error -> ErrorState(
            title = "We couldn’t load your account",
            message = if (state.hasCachedSavedPrograms) {
                "Your saved programs are still available. Try refreshing your account details."
            } else {
                "Check your connection and try again."
            },
            actionLabel = if (state.hasCachedSavedPrograms) "Saved programs" else "Retry",
            onActionClick = if (state.hasCachedSavedPrograms) {
                onSavedProgramsClick
            } else {
                viewModel::refresh
            },
            modifier = modifier
        )
        is ProfileState.Authenticated -> LoggedInProfileContent(
            profile = state.profile,
            savedCount = if (savedState.hasLoadedFromServer) {
                savedState.items.size
            } else {
                state.profile.savedProgramCount
            },
            onSavedProgramsClick = onSavedProgramsClick,
            onLogoutClick = viewModel::logout,
            modifier = modifier
        )
    }
}

@Composable
internal fun LoggedOutProfileContent(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
    message: String? = null
) {
    DegreeWikiScreen(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            ScreenHero(
                title = "Your DegreeWiki account",
                subtitle = "Build a shortlist you can return to whenever you are ready."
            )
        }
        message?.let {
            item {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        item {
            DegreeWikiCard {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    AccountBenefitRow(Icons.Filled.Bookmark, "Save programs")
                    AccountBenefitRow(Icons.AutoMirrored.Filled.ListAlt, "Keep your study shortlist")
                    AccountBenefitRow(Icons.Filled.CloudDone, "Continue across devices")
                    AccountBenefitRow(Icons.Filled.Lock, "Access Fit Finder later")
                }
            }
        }
        item {
            Button(onClick = onLoginClick, modifier = Modifier.fillMaxWidth()) {
                Text("Log in")
            }
        }
        item {
            Text(
                "Use your existing DegreeWiki email and password.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AccountBenefitRow(icon: ImageVector, label: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun LoggedInProfileContent(
    profile: com.example.degreewiki.domain.model.UserProfile,
    savedCount: Int,
    onSavedProgramsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DegreeWikiScreen(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            ScreenHero(
                title = "Your DegreeWiki account",
                subtitle = "Your shortlist and account details in one place."
            )
        }
        item {
            ProfileIdentityCard(
                identity = profile.visibleIdentity,
                email = profile.email
            )
        }
        item {
            SavedProgramsSummaryCard(
                count = savedCount,
                onClick = onSavedProgramsClick
            )
        }
        item {
            DegreeWikiCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Future tools", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Fit Finder will appear here when it is ready.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        item {
            OutlinedButton(onClick = onLogoutClick, modifier = Modifier.fillMaxWidth()) {
                Text("Log out")
            }
        }
    }
}

@Composable
fun ProfileIdentityCard(identity: String, email: String?) {
    DegreeWikiCard {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Account", style = MaterialTheme.typography.labelLarge)
            Text(
                identity,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            email?.takeIf { it.isNotBlank() && it != identity }?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SavedProgramsSummaryCard(count: Int, onClick: () -> Unit) {
    DegreeWikiCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Saved programs", style = MaterialTheme.typography.titleMedium)
                Text(
                    "$count in your shortlist",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                Icons.Filled.Bookmark,
                contentDescription = "Open saved programs",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
