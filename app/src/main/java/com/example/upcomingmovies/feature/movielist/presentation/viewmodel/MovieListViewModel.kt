package com.example.upcomingmovies.feature.movielist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upcomingmovies.feature.movielist.domain.usecase.ObserveMoviesUseCase
import com.example.upcomingmovies.feature.movielist.domain.usecase.RefreshMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val observeMoviesUseCase: ObserveMoviesUseCase,
    private val refreshMoviesUseCase: RefreshMoviesUseCase,
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
            observeMoviesUseCase().collect { movies ->
                val current = _state.value
                if (movies.isNotEmpty() || current is MovieListState.Success) {
                    _state.value = MovieListState.Success(movies)
                }
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            runCatching { refreshMoviesUseCase() }
                .onFailure { error ->
                    if (_state.value !is MovieListState.Success) {
                        _state.value = MovieListState.Error(
                            error.message ?: "Failed to load movies"
                        )
                    }
                }
        }
    }
}
