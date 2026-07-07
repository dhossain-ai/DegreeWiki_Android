package com.example.degreewiki.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/** Represents the app-level authentication state. */
sealed interface AuthState {
    data object Loading : AuthState
    data class Authenticated(val userId: String) : AuthState
    data object Unauthenticated : AuthState
}

/** Repository that owns authentication operations and exposes auth state. */
interface AuthRepository {
    val authState: StateFlow<AuthState>
    suspend fun loginWithEmail(email: String, password: String)
    suspend fun logout()
}

@Singleton
class DefaultAuthRepository @Inject constructor(
    private val supabaseClient: SupabaseClient
) : AuthRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override val authState: StateFlow<AuthState> =
        supabaseClient.auth.sessionStatus
            .map { status ->
                when (status) {
                    is SessionStatus.Authenticated ->
                        AuthState.Authenticated(
                            userId = status.session.user?.id ?: ""
                        )
                    is SessionStatus.NotAuthenticated ->
                        AuthState.Unauthenticated
                    is SessionStatus.Initializing ->
                        AuthState.Loading
                    is SessionStatus.RefreshFailure ->
                        AuthState.Unauthenticated
                }
            }
            .stateIn(scope, SharingStarted.Eagerly, AuthState.Loading)

    override suspend fun loginWithEmail(email: String, password: String) {
        withContext(Dispatchers.IO) {
            supabaseClient.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        }
    }

    override suspend fun logout() {
        withContext(Dispatchers.IO) {
            supabaseClient.auth.signOut()
        }
    }
}
