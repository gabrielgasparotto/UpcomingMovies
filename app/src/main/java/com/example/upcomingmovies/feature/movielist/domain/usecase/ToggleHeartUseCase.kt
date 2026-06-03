package com.example.upcomingmovies.feature.movielist.domain.usecase

import com.example.upcomingmovies.feature.movielist.domain.repository.HeartRepository

class ToggleHeartUseCase(private val repository: HeartRepository) {
    suspend operator fun invoke(movieId: Int) = repository.toggleHeart(movieId)
}
