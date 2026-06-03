package com.example.upcomingmovies.feature.movielist.di

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE movies ADD COLUMN voteCount INTEGER NOT NULL DEFAULT 0")
    }
}

private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `hearted_movies` (`movieId` INTEGER NOT NULL, PRIMARY KEY(`movieId`))"
        )
    }
}

val movieListModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            DATABASE_NAME,
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
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
