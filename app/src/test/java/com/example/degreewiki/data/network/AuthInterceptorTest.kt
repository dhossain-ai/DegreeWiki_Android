package com.example.degreewiki.data.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AuthInterceptorTest {
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun protectedRequestGetsBearerHeaderAndMarkerIsRemoved() {
        server.enqueue(MockResponse().setResponseCode(200))
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(AccessTokenProvider { "access-token" }))
            .build()

        client.newCall(
            Request.Builder()
                .url(server.url("/api/mobile/me"))
                .header(AUTHENTICATED_REQUEST_HEADER, "true")
                .build()
        ).execute().close()

        val request = server.takeRequest()
        assertEquals("Bearer access-token", request.getHeader("Authorization"))
        assertNull(request.getHeader(AUTHENTICATED_REQUEST_HEADER))
    }

    @Test
    fun publicRequestDoesNotGetBearerHeader() {
        server.enqueue(MockResponse().setResponseCode(200))
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(AccessTokenProvider { "access-token" }))
            .build()

        client.newCall(
            Request.Builder().url(server.url("/api/mobile/programs")).build()
        ).execute().close()

        assertNull(server.takeRequest().getHeader("Authorization"))
    }
}
