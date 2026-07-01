package com.example.upcomingmovies.feature.moviedetails.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upcomingmovies.feature.core.domain.MoviesError
import com.example.upcomingmovies.feature.core.domain.MoviesException
import com.example.upcomingmovies.feature.core.presentation.MoviesErrorMessageMapper
import com.example.upcomingmovies.feature.moviedetails.domain.usecase.GetMovieDetailUseCase
import com.example.upcomingmovies.feature.movielist.domain.usecase.ObserveHeartedIdsUseCase
import com.example.upcomingmovies.feature.movielist.domain.usecase.ToggleHeartUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieId: Int,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val toggleHeartUseCase: ToggleHeartUseCase,
    observeHeartedIdsUseCase: ObserveHeartedIdsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<MovieDetailState>(MovieDetailState.Loading)
    val state: StateFlow<MovieDetailState> = _state.asStateFlow()

    val isHearted: StateFlow<Boolean> = observeHeartedIdsUseCase()
        .map { movieId in it }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        loadDetail()
    }

    fun onAction(action: MovieDetailAction) {
        when (action) {
            MovieDetailAction.RetryLoad -> loadDetail()
            MovieDetailAction.NavigateBack -> Unit
            MovieDetailAction.ToggleHeart -> viewModelScope.launch {
                toggleHeartUseCase(movieId)
            }
        }
    }

    private fun loadDetail() {
        viewModelScope.launch {
            _state.value = MovieDetailState.Loading
            runCatching { getMovieDetailUseCase(movieId) }
                .onSuccess { _state.value = MovieDetailState.Success(it) }
                .onFailure { throwable ->
                    val error = (throwable as? MoviesException)?.error ?: MoviesError.Unknown
                    _state.value = MovieDetailState.Error(MoviesErrorMessageMapper.map(error))
                }
        }
    }
}
