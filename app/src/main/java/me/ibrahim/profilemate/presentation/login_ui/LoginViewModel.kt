package me.ibrahim.profilemate.presentation.login_ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import me.ibrahim.profilemate.data.dto.LoginRequest
import me.ibrahim.profilemate.data.remote.NetworkResponse
import me.ibrahim.profilemate.domain.models.User
import me.ibrahim.profilemate.domain.use_cases.login.LoginUseCase
import me.ibrahim.profilemate.domain.use_cases.main.SaveTokenUseCase
import me.ibrahim.profilemate.domain.use_cases.profile.SaveUserUseCase
import me.ibrahim.profilemate.domain.utils.DispatchersProvider
import me.ibrahim.profilemate.utils.getGravatarUrl
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    private val _loginMutableStateFlow = MutableStateFlow<LoginStates>(LoginStates.Initial)
    val loginStateFlow = _loginMutableStateFlow.asStateFlow()

    fun onEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.LoginClicked -> {
                login(LoginRequest(email = event.email, password = event.password))
            }

            is LoginScreenEvent.SaveToken -> saveToken(event.token)
            is LoginScreenEvent.SaveUser -> saveUser(event.user)
        }
    }

    private fun saveUser(user: User) {
        viewModelScope.launch(dispatchersProvider.io) {
            saveUserUseCase(user)
        }
    }

    private fun saveToken(token: String) {
        viewModelScope.launch(dispatchersProvider.io) {
            saveTokenUseCase(token)
        }
    }

    fun login(loginRequest: LoginRequest) {

        viewModelScope.launch(dispatchersProvider.io) {
            _loginMutableStateFlow.value = LoginStates.Loading

            loginUseCase(loginRequest)
                .catch { throwable ->
                    _loginMutableStateFlow.value = LoginStates.Error(error = throwable.localizedMessage ?: "Unknown Error")
                }.collect { response ->
                    when (response) {
                        is NetworkResponse.Error -> {
                            _loginMutableStateFlow.value = LoginStates.Error(error = response.errorMsg ?: "Unknown Error")
                        }

                        is NetworkResponse.Loading -> {
                            _loginMutableStateFlow.value = LoginStates.Loading
                        }

                        is NetworkResponse.Success -> {
                            val loginResponse = response.data
                            val user = User(
                                userId = loginResponse.userid,
                                email = loginRequest.email,
                                password = loginRequest.password,
                                avatarUrl = loginRequest.email.getGravatarUrl()
                            )
                            _loginMutableStateFlow.value = LoginStates.Success(token = loginResponse.token, user = user)
                        }
                    }
                }
        }
    }


}