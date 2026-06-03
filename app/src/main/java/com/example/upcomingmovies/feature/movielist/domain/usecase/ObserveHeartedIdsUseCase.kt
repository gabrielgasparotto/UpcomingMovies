package com.example.upcomingmovies.feature.movielist.domain.usecase

import com.example.upcomingmovies.feature.movielist.domain.repository.HeartRepository
import kotlinx.coroutines.flow.Flow

class ObserveHeartedIdsUseCase(private val repository: HeartRepository) {
    operator fun invoke(): Flow<Set<Int>> = repository.observeHeartedIds()
}
