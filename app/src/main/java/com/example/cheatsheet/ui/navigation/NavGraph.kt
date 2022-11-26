package com.example.cheatsheet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cheatsheet.ui.screen.DetailScreen
import com.example.cheatsheet.ui.screen.HomeScreen

const val TEXT_KEY = "text_key"

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Screens.HomeScreen.route) {


        composable(route = Screens.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(
            route = Screens.DetailScreen.route + "/{${TEXT_KEY}}",
            arguments = listOf(
                navArgument(name = TEXT_KEY) {
                    type = NavType.StringType
                }
            )
        ) { entry ->
            DetailScreen(entry.arguments?.getString(TEXT_KEY) ?: "")
        }
    }
}