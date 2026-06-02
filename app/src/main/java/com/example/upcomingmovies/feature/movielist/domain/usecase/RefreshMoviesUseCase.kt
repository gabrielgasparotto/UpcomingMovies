package com.example.upcomingmovies.feature.movielist.domain.usecase

import com.example.upcomingmovies.feature.movielist.domain.repository.MovieRepository

class RefreshMoviesUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke() = repository.refresh()
}
