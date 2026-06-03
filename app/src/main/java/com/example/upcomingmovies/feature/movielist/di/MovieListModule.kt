package com.example.upcomingmovies.feature.movielist.di

import androidx.room.Room
import com.example.upcomingmovies.feature.core.data.AppDatabase
import com.example.upcomingmovies.feature.movielist.data.remote.MovieService
import com.example.upcomingmovies.feature.movielist.data.repository.HeartRepositoryImpl
import com.example.upcomingmovies.feature.movielist.data.repository.MovieRepositoryImpl
import com.example.upcomingmovies.feature.movielist.domain.repository.HeartRepository
import com.example.upcomingmovies.feature.movielist.domain.repository.MovieRepository
import com.example.upcomingmovies.feature.movielist.domain.usecase.ObserveHeartedIdsUseCase
import com.example.upcomingmovies.feature.movielist.domain.usecase.ObserveMoviesUseCase
import com.example.upcomingmovies.feature.movielist.domain.usecase.RefreshMoviesUseCase
import com.example.upcomingmovies.feature.movielist.domain.usecase.ToggleHeartUseCase
import com.example.upcomingmovies.feature.movielist.presentation.viewmodel.MovieListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

private const val DATABASE_NAME = "upcoming_movies.db"

val movieListModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            DATABASE_NAME,
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }
    single { get<AppDatabase>().movieDao() }
    single { get<AppDatabase>().heartDao() }
    single { get<Retrofit>().create(MovieService::class.java) }
    single<MovieRepository> { MovieRepositoryImpl(get(), get()) }
    single<HeartRepository> { HeartRepositoryImpl(get()) }
    factory { ObserveMoviesUseCase(get()) }
    factory { RefreshMoviesUseCase(get()) }
    factory { ObserveHeartedIdsUseCase(get()) }
    factory { ToggleHeartUseCase(get()) }

    viewModel { MovieListViewModel(get(), get(), get()) }
}
