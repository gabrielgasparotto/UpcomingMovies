package com.example.upcomingmovies.feature.moviedetails.domain.repository

import com.example.upcomingmovies.feature.moviedetails.domain.model.MovieDetail

interface MovieDetailRepository {
    suspend fun getMovieDetail(movieId: Int): MovieDetail
}
