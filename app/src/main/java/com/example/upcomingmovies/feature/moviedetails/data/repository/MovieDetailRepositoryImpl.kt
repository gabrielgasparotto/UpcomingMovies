package com.example.upcomingmovies.feature.moviedetails.data.repository

import com.example.upcomingmovies.feature.moviedetails.data.mapper.toDomain
import com.example.upcomingmovies.feature.moviedetails.data.remote.MovieDetailService
import com.example.upcomingmovies.feature.moviedetails.domain.model.MovieDetail
import com.example.upcomingmovies.feature.moviedetails.domain.repository.MovieDetailRepository

class MovieDetailRepositoryImpl(
    private val service: MovieDetailService,
) : MovieDetailRepository {
    override suspend fun getMovieDetail(movieId: Int): MovieDetail =
        service.getMovieDetail(movieId).toDomain()
}
