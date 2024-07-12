package com.aigak.gifviewer.navigation

import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aigak.gifviewer.ui.screens.DetailsScreen
import com.aigak.gifviewer.ui.screens.MainScreen
import timber.log.Timber


@Composable
fun NavigationController(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = Main.route

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Main.route) {
            MainScreen(modifier = modifier, navController = navController)
        }
        composable(Details.createRoute(), arguments = listOf(
            navArgument(
                name = Details.urlKey
            ) {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument(
                name = Details.titleKey
            ) {
                type = NavType.StringType
                defaultValue = ""
            }
        ),
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { 1800 }
                )
            }) {
            val url = it.arguments?.getString(Details.urlKey)
                ?.replace("{", "")?.replace("}", "")
            val title = it.arguments?.getString(Details.titleKey)
                ?.replace("{", "")?.replace("}", "")

            DetailsScreen(modifier = modifier, url = url, title = title)
        }
    }
}