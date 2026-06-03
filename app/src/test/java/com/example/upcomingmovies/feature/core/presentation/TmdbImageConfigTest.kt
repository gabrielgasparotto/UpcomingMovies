package com.example.upcomingmovies.feature.core.presentation

import org.junit.Assert
import org.junit.Test

class TmdbImageConfigTest {

    // region constants

    @Test
    fun `POSTER_SMALL - contains correct size and base url`() {
        Assert.assertEquals("https://image.tmdb.org/t/p/w185", TmdbImageConfig.POSTER_SMALL)
    }

    @Test
    fun `POSTER_LARGE - contains correct size and base url`() {
        Assert.assertEquals("https://image.tmdb.org/t/p/w342", TmdbImageConfig.POSTER_LARGE)
    }

    @Test
    fun `BACKDROP - contains correct size and base url`() {
        Assert.assertEquals("https://image.tmdb.org/t/p/w780", TmdbImageConfig.BACKDROP)
    }

    // endregion

    // region posterSmallUrl

    @Test
    fun `posterSmallUrl - null path - returns null`() {
        Assert.assertNull(TmdbImageConfig.posterSmallUrl(null))
    }

    @Test
    fun `posterSmallUrl - valid path - returns full url with w185 size`() {
        val result = TmdbImageConfig.posterSmallUrl("/mIBCtPvKZQlxubxKMeViO2UrP3q.jpg")
        Assert.assertEquals(
            "https://image.tmdb.org/t/p/w185/mIBCtPvKZQlxubxKMeViO2UrP3q.jpg",
            result
        )
    }

    // endregion

    // region posterLargeUrl

    @Test
    fun `posterLargeUrl - null path - returns null`() {
        Assert.assertNull(TmdbImageConfig.posterLargeUrl(null))
    }

    @Test
    fun `posterLargeUrl - valid path - returns full url with w342 size`() {
        val result = TmdbImageConfig.posterLargeUrl("/6FfCtAuVAW8XJjZ7eWeLibRLWTw.jpg")
        Assert.assertEquals(
            "https://image.tmdb.org/t/p/w342/6FfCtAuVAW8XJjZ7eWeLibRLWTw.jpg",
            result
        )
    }

    // endregion

    // region backdropUrl

    @Test
    fun `backdropUrl - null path - returns null`() {
        Assert.assertNull(TmdbImageConfig.backdropUrl(null))
    }

    @Test
    fun `backdropUrl - valid path - returns full url with w780 size`() {
        val result = TmdbImageConfig.backdropUrl("/2w4xG178RpB4MDAIfTkqAuSJzec.jpg")
        Assert.assertEquals(
            "https://image.tmdb.org/t/p/w780/2w4xG178RpB4MDAIfTkqAuSJzec.jpg",
            result
        )
    }

    // endregion
}