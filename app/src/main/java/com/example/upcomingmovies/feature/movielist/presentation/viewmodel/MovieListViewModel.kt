package com.example.upcomingmovies.feature.movielist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upcomingmovies.feature.movielist.domain.usecase.ObserveHeartedIdsUseCase
import com.example.upcomingmovies.feature.movielist.domain.usecase.ObserveMoviesUseCase
import com.example.upcomingmovies.feature.movielist.domain.usecase.RefreshMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val observeMoviesUseCase: ObserveMoviesUseCase,
    private val refreshMoviesUseCase: RefreshMoviesUseCase,
    private val observeHeartedIdsUseCase: ObserveHeartedIdsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<MovieListState>(MovieListState.Loading)
    val state: StateFlow<MovieListState> = _state.asStateFlow()

    init {
        observeMovies()
        onAction(MovieListAction.Refresh)
    }

    fun onAction(action: MovieListAction) {
        when (action) {
            MovieListAction.Refresh -> refresh()
            MovieListAction.RetryLoad -> {
                _state.value = MovieListState.Loading
                refresh()
            }
        }
    }

    private fun observeMovies() {
        viewModelScope.launch {
            combine(
                observeMoviesUseCase(),
                observeHeartedIdsUseCase(),
            ) { movies, heartedIds ->
                movies.map { it.copy(isHearted = it.id in heartedIds) }
            }.collect { movies ->
                val current = _state.value
                if (movies.isNotEmpty()) {
                    val favorites = movies.filter { it.isHearted }
                    _state.value = MovieListState.Success(
                        allMoviesTab = MovieListTabContent.Movies(movies),
                        favoritesTab = if (favorites.isEmpty()) MovieListTabContent.Empty
                                       else MovieListTabContent.Movies(favorites),
                    )
                } else if (current is MovieListState.Success || current is MovieListState.Empty) {
                    _state.value = MovieListState.Empty
                }
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            runCatching { refreshMoviesUseCase() }
                .onFailure {
                    if (_state.value !is MovieListState.Success) {
                        _state.value = MovieListState.Error
                    }
                }
        }
    }
}
