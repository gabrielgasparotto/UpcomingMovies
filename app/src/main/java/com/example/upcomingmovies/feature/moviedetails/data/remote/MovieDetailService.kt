package com.example.upcomingmovies.feature.moviedetails.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface MovieDetailService {
    @GET("movie/{movieId}")
    suspend fun getMovieDetail(@Path("movieId") movieId: Int): MovieDetailDto
}
