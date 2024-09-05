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
import me.ibrahim.profilemate.domain.use_cases.ReadTokenUseCase
import me.ibrahim.profilemate.presentation.navigation.Routes
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(readTokenUseCase: ReadTokenUseCase) : ViewModel() {

    var splashScreenHold by mutableStateOf(true)
        private set

    var startDestination by mutableStateOf(Routes.NavAppLogin.route)
        private set

    init {
        readTokenUseCase().onEach { token ->
            startDestination = if (token.isEmpty()) {
                Routes.NavAppLogin.route
            } else {
                Routes.NavAppProfile.route
            }
            splashScreenHold = false
        }.launchIn(viewModelScope)
    }

}