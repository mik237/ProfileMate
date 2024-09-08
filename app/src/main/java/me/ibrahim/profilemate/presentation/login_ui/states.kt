package me.ibrahim.profilemate.presentation.login_ui

import me.ibrahim.profilemate.domain.models.User

sealed class LoginStates {
    data object Initial : LoginStates()
    data object Loading : LoginStates()
    data class Success(val token: String, val user: User) : LoginStates()
    data class Error(val error: String) : LoginStates()
}