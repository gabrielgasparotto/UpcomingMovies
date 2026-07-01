package com.example.upcomingmovies.feature.moviedetails.data.repository

import com.example.upcomingmovies.feature.core.data.MoviesErrorMapper
import com.example.upcomingmovies.feature.core.domain.MoviesException
import com.example.upcomingmovies.feature.moviedetails.data.mapper.toDomain
import com.example.upcomingmovies.feature.moviedetails.data.remote.MovieDetailService
import com.example.upcomingmovies.feature.moviedetails.domain.model.MovieDetail
import com.example.upcomingmovies.feature.moviedetails.domain.repository.MovieDetailRepository

class MovieDetailRepositoryImpl(
    private val service: MovieDetailService,
) : MovieDetailRepository {
    override suspend fun getMovieDetail(movieId: Int): MovieDetail =
        runCatching { service.getMovieDetail(movieId).toDomain() }
            .getOrElse { throw MoviesException(MoviesErrorMapper.map(it)) }
}
