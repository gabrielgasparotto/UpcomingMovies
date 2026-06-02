package com.example.upcomingmovies.feature.movielist.data.remote

import retrofit2.http.GET

interface MovieService {
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(): UpcomingMoviesResponse
}
