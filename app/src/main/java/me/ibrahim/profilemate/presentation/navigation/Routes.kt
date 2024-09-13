package me.ibrahim.profilemate.presentation.navigation


sealed class Routes(val route: String) {
    data object LoginScreen : Routes("loginScreen")
    data object ProfileScreen : Routes("profileScreen")
}