package com.example.upcomingmovies.feature.movielist.data.mapper

import com.example.upcomingmovies.feature.movielist.data.local.MovieEntity
import com.example.upcomingmovies.feature.movielist.data.remote.MovieDto
import com.example.upcomingmovies.feature.movielist.domain.model.Movie
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MovieMapperTest {

    // region MovieDto.toEntity

    @Test
    fun `movieDtoToEntity - all fields - mapped correctly`() {
        // Given
        val dto = buildMovieDto()

        // When
        val entity = dto.toEntity()

        // Then
        assertEquals(dto.id, entity.id)
        assertEquals(dto.title, entity.title)
        assertEquals(dto.overview, entity.overview)
        assertEquals(dto.releaseDate, entity.releaseDate)
        assertEquals(dto.posterPath, entity.posterPath)
        assertEquals(dto.voteAverage, entity.voteAverage, 0.0)
        assertEquals(dto.voteCount, entity.voteCount)
    }

    @Test
    fun `movieDtoToEntity - null posterPath - preserved as null`() {
        // Given
        val dto = buildMovieDto(posterPath = null)

        // When
        val entity = dto.toEntity()

        // Then
        assertNull(entity.posterPath)
    }

    // endregion

    // region MovieEntity.toDomain

    @Test
    fun `movieEntityToDomain - all fields - mapped correctly`() {
        // Given
        val entity = buildMovieEntity()

        // When
        val domain = entity.toDomain()

        // Then
        assertEquals(entity.id, domain.id)
        assertEquals(entity.title, domain.title)
        assertEquals(entity.overview, domain.overview)
        assertEquals(entity.releaseDate, domain.releaseDate)
        assertEquals(entity.posterPath, domain.posterPath)
        assertEquals(entity.voteAverage, domain.voteAverage, 0.0)
        assertEquals(entity.voteCount, domain.voteCount)
    }

    @Test
    fun `movieEntityToDomain - null posterPath - preserved as null`() {
        // Given
        val entity = buildMovieEntity(posterPath = null)

        // When
        val domain = entity.toDomain()

        // Then
        assertNull(domain.posterPath)
    }

    @Test
    fun `movieDtoToEntity toDomain - round trip - produces equivalent domain model`() {
        // Given
        val dto = buildMovieDto()
        val expected = Movie(
            id = dto.id,
            title = dto.title,
            overview = dto.overview,
            releaseDate = dto.releaseDate,
            posterPath = dto.posterPath,
            voteAverage = dto.voteAverage,
            voteCount = dto.voteCount,
        )

        // When
        val result = dto.toEntity().toDomain()

        // Then
        assertEquals(expected, result)
    }

    // endregion
}

private fun buildMovieDto(posterPath: String? = "/poster.jpg") = MovieDto(
    id = 1,
    title = "Evil Dead Rise",
    originalTitle = "Evil Dead Rise",
    overview = "Two sisters find an ancient vinyl...",
    releaseDate = "2023-04-12",
    posterPath = posterPath,
    backdropPath = "/backdrop.jpg",
    voteAverage = 7.0,
    voteCount = 1234,
    popularity = 80.5,
    genreIds = listOf(27, 53),
    adult = false,
    video = false,
    originalLanguage = "en",
)

private fun buildMovieEntity(posterPath: String? = "/poster.jpg") = MovieEntity(
    id = 1,
    title = "Evil Dead Rise",
    overview = "Two sisters find an ancient vinyl...",
    releaseDate = "2023-04-12",
    posterPath = posterPath,
    voteAverage = 7.0,
    voteCount = 1234,
)
