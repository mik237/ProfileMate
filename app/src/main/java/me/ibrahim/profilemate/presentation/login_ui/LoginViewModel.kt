package me.ibrahim.profilemate.presentation.login_ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.ibrahim.profilemate.data.api.NetworkResponse
import me.ibrahim.profilemate.data.dto.LoginRequest
import me.ibrahim.profilemate.domain.use_cases.LoginUseCase
import me.ibrahim.profilemate.domain.use_cases.SaveTokenUseCase
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val saveTokenUseCase: SaveTokenUseCase
) : ViewModel() {

    private val _loginMutableStateFlow = MutableStateFlow<LoginStates>(LoginStates.Initial)
    val loginStateFlow = _loginMutableStateFlow.asStateFlow()

    fun onEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.LoginClicked -> {
                login(LoginRequest(email = event.email, password = event.password))
            }
        }
    }

    private fun login(loginRequest: LoginRequest) {

        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase(loginRequest).collect { response ->
                when (response) {
                    is NetworkResponse.Error -> {
                        _loginMutableStateFlow.value = LoginStates.Error(error = response.message ?: "Unknown Error")
                    }

                    is NetworkResponse.Loading -> {
                        _loginMutableStateFlow.value = LoginStates.Loading
                    }

                    is NetworkResponse.Success -> {
                        val loginResponse = response.data
                        saveTokenUseCase(loginResponse.token)
                        _loginMutableStateFlow.value = LoginStates.Success
                    }
                }
            }
        }
    }
}