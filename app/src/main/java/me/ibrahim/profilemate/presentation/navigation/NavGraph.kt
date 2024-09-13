package me.ibrahim.profilemate.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.ibrahim.profilemate.presentation.login_ui.LoginScreen
import me.ibrahim.profilemate.presentation.profile_ui.ProfileScreen


@Composable
fun NavGraph(startDestination: String) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {

        composable(route = Routes.LoginScreen.route) {
            LoginScreen()
        }

        composable(route = Routes.ProfileScreen.route) {
            ProfileScreen()
        }
    }
}