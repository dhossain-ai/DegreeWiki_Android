package com.example.degreewiki.data.network

import android.content.SharedPreferences
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * A [SessionManager] backed by [EncryptedSharedPreferences] so that
 * the Supabase JWT (access + refresh tokens) is stored at rest with
 * AES-256 encryption.
 *
 * When no session is stored, [loadSession] throws an exception which
 * the SDK catches internally to emit [SessionStatus.NotAuthenticated].
 */
class SecureSessionManager(
    private val prefs: SharedPreferences,
    private val json: Json = Json { ignoreUnknownKeys = true }
) : SessionManager {

    override suspend fun loadSession(): UserSession {
        val raw = prefs.getString(KEY_SESSION, null)
            ?: error("No session stored")
        return try {
            json.decodeFromString<UserSession>(raw)
        } catch (e: Exception) {
            // Corrupt / incompatible session data — wipe and signal no session
            prefs.edit().remove(KEY_SESSION).apply()
            error("Corrupt session data: ${e.message}")
        }
    }

    override suspend fun saveSession(session: UserSession) {
        val raw = json.encodeToString(session)
        prefs.edit().putString(KEY_SESSION, raw).apply()
    }

    override suspend fun deleteSession() {
        prefs.edit().remove(KEY_SESSION).apply()
    }

    private companion object {
        const val KEY_SESSION = "supabase_session"
    }
}


