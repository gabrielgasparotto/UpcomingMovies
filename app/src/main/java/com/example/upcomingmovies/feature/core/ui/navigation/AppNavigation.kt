package com.example.upcomingmovies.feature.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.upcomingmovies.feature.moviedetails.presentation.MovieDetailRoute
import com.example.upcomingmovies.feature.movielist.presentation.MovieListRoute

private object Routes {
    const val MOVIE_LIST = "movie_list"
    const val MOVIE_DETAIL = "movie_detail/{movieId}"
    fun movieDetail(movieId: Int) = "movie_detail/$movieId"
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.MOVIE_LIST,
        modifier = modifier,
    ) {
        composable(Routes.MOVIE_LIST) {
            MovieListRoute(
                onMovieClick = { movieId -> navController.navigate(Routes.movieDetail(movieId)) }
            )
        }
        composable(
            route = Routes.MOVIE_DETAIL,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
            MovieDetailRoute(
                movieId = movieId,
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}
