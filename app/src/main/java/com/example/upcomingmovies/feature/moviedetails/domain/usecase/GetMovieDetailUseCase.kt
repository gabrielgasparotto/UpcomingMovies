package com.example.upcomingmovies.feature.moviedetails.domain.usecase

import com.example.upcomingmovies.feature.moviedetails.domain.model.MovieDetail
import com.example.upcomingmovies.feature.moviedetails.domain.repository.MovieDetailRepository

class GetMovieDetailUseCase(private val repository: MovieDetailRepository) {
    suspend operator fun invoke(movieId: Int): MovieDetail = repository.getMovieDetail(movieId)
}
