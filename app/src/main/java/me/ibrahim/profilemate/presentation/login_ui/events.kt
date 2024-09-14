package me.ibrahim.profilemate.presentation.login_ui

sealed class LoginScreenEvent {
    data class LoginClicked(val email: String, val password: String) : LoginScreenEvent()
}