package com.example.upcomingmovies.feature.core.presentation

object TmdbImageConfig {
    private const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"

    const val POSTER_SMALL = "${BASE_IMAGE_URL}w185"
    const val POSTER_LARGE = "${BASE_IMAGE_URL}w342"
    const val BACKDROP = "${BASE_IMAGE_URL}w780"

    fun posterSmallUrl(path: String?): String? = path?.let { "$POSTER_SMALL$it" }
    fun posterLargeUrl(path: String?): String? = path?.let { "$POSTER_LARGE$it" }
    fun backdropUrl(path: String?): String? = path?.let { "$BACKDROP$it" }
}