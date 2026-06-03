package com.example.upcomingmovies.feature.moviedetails.di

import com.example.upcomingmovies.feature.moviedetails.data.remote.MovieDetailService
import com.example.upcomingmovies.feature.moviedetails.data.repository.MovieDetailRepositoryImpl
import com.example.upcomingmovies.feature.moviedetails.domain.repository.MovieDetailRepository
import com.example.upcomingmovies.feature.moviedetails.domain.usecase.GetMovieDetailUseCase
import com.example.upcomingmovies.feature.moviedetails.presentation.viewmodel.MovieDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val movieDetailModule = module {
    single { get<Retrofit>().create(MovieDetailService::class.java) }
    single<MovieDetailRepository> { MovieDetailRepositoryImpl(get()) }
    factory { GetMovieDetailUseCase(get()) }
    viewModel { params -> MovieDetailViewModel(params.get(), get(), get(), get()) }
}
