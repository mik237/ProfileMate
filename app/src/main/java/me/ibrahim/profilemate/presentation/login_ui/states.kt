package me.ibrahim.profilemate.presentation.login_ui

sealed class LoginStates {
    data object Initial : LoginStates()
    data object Loading : LoginStates()
    data object Success : LoginStates()
    data class Error(val error: String) : LoginStates()
}