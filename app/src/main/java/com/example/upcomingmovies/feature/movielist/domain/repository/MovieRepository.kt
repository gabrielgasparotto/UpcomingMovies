package com.example.upcomingmovies.feature.movielist.domain.repository

import com.example.upcomingmovies.feature.movielist.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun observeMovies(): Flow<List<Movie>>
    suspend fun refresh()
}
