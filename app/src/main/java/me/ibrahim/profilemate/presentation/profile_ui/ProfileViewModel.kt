package me.ibrahim.profilemate.presentation.profile_ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.ibrahim.profilemate.domain.models.User
import me.ibrahim.profilemate.domain.use_cases.ReadUserUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(val readUserUseCase: ReadUserUseCase) : ViewModel() {
    private val _userProfileMutableStateFlow = MutableStateFlow<User?>(null)
    val userProfileStateFlow = _userProfileMutableStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            readUserUseCase().collect { user ->
                _userProfileMutableStateFlow.value = user
            }
        }
    }
}