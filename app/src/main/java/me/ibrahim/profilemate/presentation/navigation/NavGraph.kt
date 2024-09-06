package me.ibrahim.profilemate.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import me.ibrahim.profilemate.presentation.login_ui.LoginScreen
import me.ibrahim.profilemate.presentation.profile_ui.ProfileScreen


@Composable
fun NavGraph(startDestination: String) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {

        navigation(
            route = Routes.NavAppLogin.route,
            startDestination = Routes.LoginScreen.route
        ) {
            composable(route = Routes.LoginScreen.route) {
                LoginScreen()
            }
        }

        navigation(
            route = Routes.NavAppProfile.route,
            startDestination = Routes.ProfileScreen.route
        ) {
            composable(route = Routes.ProfileScreen.route) {
                ProfileScreen()
            }
        }
    }
}