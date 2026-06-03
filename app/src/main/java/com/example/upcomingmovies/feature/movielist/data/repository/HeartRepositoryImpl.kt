package com.example.upcomingmovies.feature.movielist.data.repository

import com.example.upcomingmovies.feature.movielist.data.local.HeartDao
import com.example.upcomingmovies.feature.movielist.data.local.HeartedMovieEntity
import com.example.upcomingmovies.feature.movielist.domain.repository.HeartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class HeartRepositoryImpl(private val dao: HeartDao) : HeartRepository {

    override fun observeHeartedIds(): Flow<Set<Int>> =
        dao.observeHeartedIds().map { it.toSet() }

    override suspend fun toggleHeart(movieId: Int) {
        if (dao.exists(movieId)) dao.delete(movieId)
        else dao.insert(HeartedMovieEntity(movieId))
    }
}
