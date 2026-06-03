package com.example.upcomingmovies.feature.core.di

import com.example.upcomingmovies.BuildConfig
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import retrofit2.Retrofit

class NetworkModuleTest {

    @After
    fun tearDown() {
        stopKoin()
    }

    // region OkHttpClient — auth interceptor

    @Test
    fun `okHttpClient - auth interceptor - adds Authorization Bearer header`() {
        // Given
        val client = startKoin { modules(networkModule) }.koin.get<OkHttpClient>()
        val capturedRequest = interceptRequest(client.interceptors.first())

        // Then
        assertEquals(
            "Bearer ${BuildConfig.TMDB_ACCESS_TOKEN}",
            capturedRequest.header("Authorization"),
        )
    }

    @Test
    fun `okHttpClient - auth interceptor - adds Accept application json header`() {
        // Given
        val client = startKoin { modules(networkModule) }.koin.get<OkHttpClient>()
        val capturedRequest = interceptRequest(client.interceptors.first())

        // Then
        assertEquals("application/json", capturedRequest.header("Accept"))
    }

    @Test
    fun `okHttpClient - auth interceptor - preserves original request url and method`() {
        // Given
        val client = startKoin { modules(networkModule) }.koin.get<OkHttpClient>()
        val originalUrl = "https://api.themoviedb.org/3/movie/upcoming"
        val capturedRequest = interceptRequest(client.interceptors.first(), url = originalUrl)

        // Then — url and method are untouched
        assertEquals(originalUrl, capturedRequest.url.toString())
        assertEquals("GET", capturedRequest.method)
    }

    // endregion

    // region OkHttpClient — interceptor count

    @Test
    fun `okHttpClient - debug build - has two interceptors`() {
        // Given
        val client = startKoin { modules(networkModule) }.koin.get<OkHttpClient>()

        // Then — auth interceptor always present; logging added in DEBUG
        val expectedCount = if (BuildConfig.DEBUG) 2 else 1
        assertEquals(expectedCount, client.interceptors.size)
    }

    @Test
    fun `okHttpClient - auth interceptor - is always first in chain`() {
        // Given
        val client = startKoin { modules(networkModule) }.koin.get<OkHttpClient>()

        // When — run the first interceptor and check it adds the auth header
        val capturedRequest = interceptRequest(client.interceptors.first())

        // Then — first interceptor is the auth one, not the logger
        assertTrue(capturedRequest.header("Authorization")?.startsWith("Bearer ") == true)
    }

    // endregion

    // region Retrofit

    @Test
    fun `retrofit - baseUrl - is TMDB API v3 url`() {
        // Given
        val retrofit = startKoin { modules(networkModule) }.koin.get<Retrofit>()

        // Then
        assertEquals("https://api.themoviedb.org/3/", retrofit.baseUrl().toString())
    }

    @Test
    fun `retrofit - callFactory - is the OkHttpClient from the module`() {
        // Given
        val koin = startKoin { modules(networkModule) }.koin
        val client = koin.get<OkHttpClient>()
        val retrofit = koin.get<Retrofit>()

        // Then — same singleton instance used by Retrofit
        assertEquals(client, retrofit.callFactory())
    }

    // endregion
}

private fun interceptRequest(
    interceptor: Interceptor,
    url: String = "https://api.themoviedb.org/3/",
): Request {
    val originalRequest = Request.Builder().url(url).build()
    val captured = slot<Request>()
    val chain = mockk<Interceptor.Chain> {
        every { request() } returns originalRequest
        every { proceed(capture(captured)) } returns mockk(relaxed = true)
    }
    interceptor.intercept(chain)
    return captured.captured
}
