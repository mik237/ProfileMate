package me.ibrahim.profilemate.presentation.login_ui

import me.ibrahim.profilemate.domain.models.User

sealed class LoginScreenEvent {
    data class LoginClicked(val email: String, val password: String) : LoginScreenEvent()
    data class SaveToken(val token: String) : LoginScreenEvent()
    data class SaveUser(val user: User) : LoginScreenEvent()
}