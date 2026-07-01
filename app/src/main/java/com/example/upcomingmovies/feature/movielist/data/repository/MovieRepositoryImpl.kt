package com.example.upcomingmovies.feature.movielist.data.repository

import com.example.upcomingmovies.feature.core.data.MoviesErrorMapper
import com.example.upcomingmovies.feature.core.domain.MoviesException
import com.example.upcomingmovies.feature.movielist.data.local.MovieDao
import com.example.upcomingmovies.feature.movielist.data.mapper.toDomain
import com.example.upcomingmovies.feature.movielist.data.mapper.toEntity
import com.example.upcomingmovies.feature.movielist.data.remote.MovieService
import com.example.upcomingmovies.feature.movielist.domain.model.Movie
import com.example.upcomingmovies.feature.movielist.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl(
    private val dao: MovieDao,
    private val service: MovieService,
) : MovieRepository {

    override fun observeMovies(): Flow<List<Movie>> =
        dao.observeMovies().map { entities -> entities.map { it.toDomain() } }

    override suspend fun refresh() {
        runCatching {
            val remote = service.getUpcomingMovies()
            dao.upsertAll(remote.results.map { it.toEntity() })
        }.onFailure { throw MoviesException(MoviesErrorMapper.map(it)) }
    }
}
