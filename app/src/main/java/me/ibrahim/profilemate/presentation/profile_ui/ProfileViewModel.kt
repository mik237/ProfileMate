package me.ibrahim.profilemate.presentation.profile_ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.ibrahim.profilemate.data.remote.NetworkResponse
import me.ibrahim.profilemate.domain.use_cases.profile.GetUserUseCase
import me.ibrahim.profilemate.domain.use_cases.profile.ReadUserUseCase
import me.ibrahim.profilemate.domain.use_cases.profile.SaveUserUseCase
import me.ibrahim.profilemate.domain.use_cases.profile.UploadAvatarUseCase
import me.ibrahim.profilemate.utils.FileUtil
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val readUserUseCase: ReadUserUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val uploadAvatarUseCase: UploadAvatarUseCase,
    private val fileUtil: FileUtil
) : ViewModel() {

    private val _userProfileMutableState = MutableStateFlow(ProfileState())
    val userProfileState = _userProfileMutableState.asStateFlow()

    private val _imageUriMutableStateFlow = MutableStateFlow<Uri?>(null)
    val imageUriStateFlow = _imageUriMutableStateFlow.asStateFlow()

    fun onEvent(event: ProfileEvents) {
        when (event) {
            ProfileEvents.CreateImageFile -> createImageFile()

            ProfileEvents.GetProfile -> getProfile()

            is ProfileEvents.UploadAvatar -> {
                uploadAvatar(event.uri)
            }
        }
    }

    private fun uploadAvatar(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {

            uploadAvatarUseCase(uri = uri).collect { response ->
                when (response) {
                    is NetworkResponse.Error -> {
                        _userProfileMutableState.value = _userProfileMutableState.value.copy(
                            avatarState = AvatarState.UploadFailed(response.errorMsg)
                        )
                    }

                    is NetworkResponse.Loading -> {
                        _userProfileMutableState.value = _userProfileMutableState.value.copy(
                            avatarState = AvatarState.Uploading
                        )
                    }

                    is NetworkResponse.Success -> {
                        val user = _userProfileMutableState.value.user?.copy(avatarUrl = response.data.avatarUrl)
                        user?.let { saveUserUseCase(user = it) }
                        _userProfileMutableState.value =
                            _userProfileMutableState.value.copy(user = user, avatarState = AvatarState.UploadSuccess(response.data.avatarUrl))
                    }
                }
            }
        }

    }

    private fun createImageFile() {
        viewModelScope.launch(Dispatchers.IO) {
            _imageUriMutableStateFlow.value = fileUtil.createImageFile()
        }
    }

    private fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            getUserUseCase().collect { response ->
                when (response) {
                    is NetworkResponse.Error -> {
                        _userProfileMutableState.value = _userProfileMutableState.value.copy(
                            profileUiState = ProfileUiState.Error(response.errorMsg)
                        )
                    }

                    is NetworkResponse.Loading -> {
                        _userProfileMutableState.value = _userProfileMutableState.value.copy(
                            profileUiState = ProfileUiState.Loading
                        )
                    }

                    is NetworkResponse.Success -> {}
                }
                readUserProfile()
            }
        }
    }

    private suspend fun readUserProfile() {
        readUserUseCase().collect { user ->
            _userProfileMutableState.value = _userProfileMutableState.value.copy(
                user = user,
                profileUiState = ProfileUiState.Success
            )
        }
    }

}