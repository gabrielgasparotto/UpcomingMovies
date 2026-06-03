package com.example.upcomingmovies.feature.moviedetails.data.mapper

import com.example.upcomingmovies.feature.moviedetails.data.remote.GenreDto
import com.example.upcomingmovies.feature.moviedetails.data.remote.MovieDetailDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MovieDetailMapperTest {

    @Test
    fun `movieDetailDtoToDomain - all fields - mapped correctly`() {
        // Given
        val dto = buildMovieDetailDto()

        // When
        val domain = dto.toDomain()

        // Then
        assertEquals(dto.id, domain.id)
        assertEquals(dto.title, domain.title)
        assertEquals(dto.tagline, domain.tagline)
        assertEquals(dto.overview, domain.overview)
        assertEquals(dto.status, domain.status)
        assertEquals(dto.releaseDate, domain.releaseDate)
        assertEquals(dto.runtime, domain.runtime)
        assertEquals(dto.voteAverage, domain.voteAverage, 0.0)
        assertEquals(dto.voteCount, domain.voteCount)
        assertEquals(dto.posterPath, domain.posterPath)
        assertEquals(dto.backdropPath, domain.backdropPath)
    }

    @Test
    fun `movieDetailDtoToDomain - genres - mapped to name strings`() {
        // Given
        val dto = buildMovieDetailDto(
            genres = listOf(
                GenreDto(id = 12, name = "Adventure"),
                GenreDto(id = 28, name = "Action"),
                GenreDto(id = 878, name = "Science Fiction"),
            )
        )

        // When
        val domain = dto.toDomain()

        // Then
        assertEquals(listOf("Adventure", "Action", "Science Fiction"), domain.genres)
    }

    @Test
    fun `movieDetailDtoToDomain - empty genres list - maps to empty list`() {
        // Given
        val dto = buildMovieDetailDto(genres = emptyList())

        // When
        val domain = dto.toDomain()

        // Then
        assertEquals(emptyList<String>(), domain.genres)
    }

    @Test
    fun `movieDetailDtoToDomain - null runtime - preserved as null`() {
        // Given
        val dto = buildMovieDetailDto(runtime = null)

        // When
        val domain = dto.toDomain()

        // Then
        assertNull(domain.runtime)
    }

    @Test
    fun `movieDetailDtoToDomain - null posterPath and backdropPath - preserved as null`() {
        // Given
        val dto = buildMovieDetailDto(posterPath = null, backdropPath = null)

        // When
        val domain = dto.toDomain()

        // Then
        assertNull(domain.posterPath)
        assertNull(domain.backdropPath)
    }
}

private fun buildMovieDetailDto(
    runtime: Int? = 121,
    posterPath: String? = "/poster.jpg",
    backdropPath: String? = "/backdrop.jpg",
    genres: List<GenreDto> = listOf(GenreDto(12, "Adventure"), GenreDto(28, "Action")),
) = MovieDetailDto(
    id = 11,
    title = "Star Wars",
    originalTitle = "Star Wars",
    tagline = "A long time ago in a galaxy far, far away...",
    overview = "Princess Leia is captured and held hostage.",
    status = "Released",
    releaseDate = "1977-05-25",
    runtime = runtime,
    voteAverage = 8.2,
    voteCount = 22061,
    popularity = 95.0,
    adult = false,
    video = false,
    posterPath = posterPath,
    backdropPath = backdropPath,
    homepage = null,
    imdbId = "tt0076759",
    budget = 11000000L,
    revenue = 775398007L,
    originalLanguage = "en",
    genres = genres,
    originCountry = listOf("US"),
)
