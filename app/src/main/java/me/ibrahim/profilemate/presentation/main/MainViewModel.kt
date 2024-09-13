package me.ibrahim.profilemate.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.ibrahim.profilemate.domain.managers.SessionManager
import me.ibrahim.profilemate.domain.use_cases.profile.ReadTokenUseCase
import me.ibrahim.profilemate.presentation.navigation.Routes
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    readTokenUseCase: ReadTokenUseCase, sessionManager: SessionManager
) : ViewModel() {

    var splashScreenHold by mutableStateOf(true)
        private set

    var startDestination by mutableStateOf(Routes.LoginScreen.route)
        private set

    init {
        readTokenUseCase().onEach {
            val isActiveSession = sessionManager.isActiveSession()
            startDestination = if (isActiveSession) {
                Routes.ProfileScreen.route
            } else {
                Routes.LoginScreen.route
            }
            delay(300)
            splashScreenHold = false
        }.launchIn(viewModelScope)
    }

}