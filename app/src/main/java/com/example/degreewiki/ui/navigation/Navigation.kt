package com.example.degreewiki.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.degreewiki.data.repository.AuthState
import com.example.degreewiki.ui.features.auth.AuthViewModel
import com.example.degreewiki.ui.features.auth.LoginScreen
import com.example.degreewiki.ui.features.main.MainScreen

@Composable
fun MainNavigation(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    // Determine the start destination based on auth state.
    // While loading, show a centered progress indicator.
    when (authState) {
        is AuthState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        is AuthState.Unauthenticated -> {
            val backStack = rememberNavBackStack(Login)

            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider = entryProvider {
                    entry<Login> {
                        LoginScreen(
                            modifier = Modifier
                                .safeDrawingPadding()
                        )
                    }
                }
            )
        }

        is AuthState.Authenticated -> {
            val backStack = rememberNavBackStack(Main)

            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider = entryProvider {
                    entry<Main> {
                        MainScreen(
                            onItemClick = { navKey -> backStack.add(navKey) },
                            modifier = Modifier
                                .safeDrawingPadding()
                                .padding(16.dp)
                        )
                    }
                }
            )
        }
    }
}

