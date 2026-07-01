package com.example.upcomingmovies.feature.core.presentation

import com.example.upcomingmovies.R
import com.example.upcomingmovies.feature.core.domain.MoviesError
import org.junit.Assert.assertEquals
import org.junit.Test

class MoviesErrorMessageMapperTest {

    @Test
    fun `map - NoNetwork - returns error_no_network`() {
        assertEquals(R.string.error_no_network, MoviesErrorMessageMapper.map(MoviesError.NoNetwork))
    }

    @Test
    fun `map - NotFound - returns error_not_found`() {
        assertEquals(R.string.error_not_found, MoviesErrorMessageMapper.map(MoviesError.NotFound))
    }

    @Test
    fun `map - ServerError - returns error_server`() {
        assertEquals(R.string.error_server, MoviesErrorMessageMapper.map(MoviesError.ServerError))
    }

    @Test
    fun `map - Unknown - returns error_unknown`() {
        assertEquals(R.string.error_unknown, MoviesErrorMessageMapper.map(MoviesError.Unknown))
    }
}
