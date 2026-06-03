package com.example.upcomingmovies.feature.moviedetails.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upcomingmovies.feature.moviedetails.domain.usecase.GetMovieDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieId: Int,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<MovieDetailState>(MovieDetailState.Loading)
    val state: StateFlow<MovieDetailState> = _state.asStateFlow()

    init {
        loadDetail()
    }

    fun onAction(action: MovieDetailAction) {
        when (action) {
            MovieDetailAction.RetryLoad -> loadDetail()
            MovieDetailAction.NavigateBack -> Unit
        }
    }

    private fun loadDetail() {
        viewModelScope.launch {
            _state.value = MovieDetailState.Loading
            runCatching { getMovieDetailUseCase(movieId) }
                .onSuccess { _state.value = MovieDetailState.Success(it) }
                .onFailure { _state.value = MovieDetailState.Error }
        }
    }
}
