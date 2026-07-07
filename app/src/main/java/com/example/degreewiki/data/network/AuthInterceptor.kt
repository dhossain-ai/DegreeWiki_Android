package com.example.degreewiki.data.network

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * OkHttp [Interceptor] that attaches the current Supabase access token
 * as an `Authorization: Bearer` header to every outgoing request.
 *
 * If no session exists the request passes through unmodified.
 */
@Singleton
class AuthInterceptor @Inject constructor(
    private val supabaseClient: SupabaseClient
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val accessToken = supabaseClient.auth.currentSessionOrNull()?.accessToken
        if (accessToken.isNullOrBlank()) {
            return chain.proceed(original)
        }

        val authenticated = original.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(authenticated)
    }
}
