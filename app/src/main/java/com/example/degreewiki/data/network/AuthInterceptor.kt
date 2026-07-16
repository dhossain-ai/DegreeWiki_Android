package com.example.degreewiki.data.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

const val AUTHENTICATED_REQUEST_HEADER = "X-DegreeWiki-Authenticated"

fun interface AccessTokenProvider {
    fun currentAccessToken(): String?
}

@Singleton
class AuthInterceptor @Inject constructor(
    private val accessTokenProvider: AccessTokenProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requiresAuthentication =
            original.header(AUTHENTICATED_REQUEST_HEADER)?.equals("true", ignoreCase = true) == true
        val requestBuilder = original.newBuilder().removeHeader(AUTHENTICATED_REQUEST_HEADER)

        if (requiresAuthentication) {
            accessTokenProvider.currentAccessToken()
                ?.takeIf(String::isNotBlank)
                ?.let { requestBuilder.header("Authorization", "Bearer $it") }
        }

        return chain.proceed(requestBuilder.build())
    }
}
