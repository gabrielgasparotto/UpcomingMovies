package com.example.upcomingmovies.feature.movielist.domain.repository

import kotlinx.coroutines.flow.Flow

interface HeartRepository {
    fun observeHeartedIds(): Flow<Set<Int>>
    suspend fun toggleHeart(movieId: Int)
}
