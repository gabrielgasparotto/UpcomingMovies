package com.example.upcomingmovies.feature.core.data

import com.example.upcomingmovies.feature.core.domain.MoviesError
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

class MoviesErrorMapperTest {

    // region IOException

    @Test
    fun `map - IOException - returns NoNetwork`() {
        val result = MoviesErrorMapper.map(IOException())
        assertEquals(MoviesError.NoNetwork, result)
    }

    @Test
    fun `map - IOException subclass - returns NoNetwork`() {
        val result = MoviesErrorMapper.map(java.net.SocketTimeoutException())
        assertEquals(MoviesError.NoNetwork, result)
    }

    // endregion

    // region HttpException

    @Test
    fun `map - HttpException 404 - returns NotFound`() {
        val result = MoviesErrorMapper.map(httpException(404))
        assertEquals(MoviesError.NotFound, result)
    }

    @Test
    fun `map - HttpException 500 - returns ServerError`() {
        val result = MoviesErrorMapper.map(httpException(500))
        assertEquals(MoviesError.ServerError, result)
    }

    @Test
    fun `map - HttpException 503 - returns ServerError`() {
        val result = MoviesErrorMapper.map(httpException(503))
        assertEquals(MoviesError.ServerError, result)
    }

    @Test
    fun `map - HttpException 599 - returns ServerError`() {
        val result = MoviesErrorMapper.map(httpException(599))
        assertEquals(MoviesError.ServerError, result)
    }

    @Test
    fun `map - HttpException 400 - returns Unknown`() {
        val result = MoviesErrorMapper.map(httpException(400))
        assertEquals(MoviesError.Unknown, result)
    }

    @Test
    fun `map - HttpException 401 - returns Unknown`() {
        val result = MoviesErrorMapper.map(httpException(401))
        assertEquals(MoviesError.Unknown, result)
    }

    // endregion

    // region unknown throwables

    @Test
    fun `map - RuntimeException - returns Unknown`() {
        val result = MoviesErrorMapper.map(RuntimeException())
        assertEquals(MoviesError.Unknown, result)
    }

    @Test
    fun `map - IllegalStateException - returns Unknown`() {
        val result = MoviesErrorMapper.map(IllegalStateException())
        assertEquals(MoviesError.Unknown, result)
    }

    // endregion

    private fun httpException(code: Int): HttpException = mockk {
        every { code() } returns code
    }
}
