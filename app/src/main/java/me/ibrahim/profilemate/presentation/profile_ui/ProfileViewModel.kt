package me.ibrahim.profilemate.presentation.profile_ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.ibrahim.profilemate.data.remote.NetworkResponse
import me.ibrahim.profilemate.domain.models.User
import me.ibrahim.profilemate.domain.use_cases.profile.GetUserUseCase
import me.ibrahim.profilemate.domain.use_cases.profile.ReadUserUseCase
import me.ibrahim.profilemate.domain.use_cases.profile.SaveUserUseCase
import me.ibrahim.profilemate.domain.use_cases.profile.UploadAvatarUseCase
import me.ibrahim.profilemate.domain.utils.DispatchersProvider
import me.ibrahim.profilemate.utils.FileUtil
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val readUserUseCase: ReadUserUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val uploadAvatarUseCase: UploadAvatarUseCase,
    private val fileUtil: FileUtil,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    private val _userProfileMutableState = MutableStateFlow(ProfileState())
    val userProfileState = _userProfileMutableState.asStateFlow()

    private val _imageUriMutableStateFlow = MutableStateFlow<Uri?>(null)
    val imageUriStateFlow = _imageUriMutableStateFlow.asStateFlow()

    private val _errorMutSharedFlow = MutableSharedFlow<String?>()
    val errorSharedFlow = _errorMutSharedFlow.asSharedFlow()

    fun onEvent(event: ProfileEvents) {
        when (event) {
            ProfileEvents.CreateImageFile -> createImageFile()

            ProfileEvents.GetProfileFromRemote -> getProfile()

            is ProfileEvents.UploadAvatar -> {
                uploadAvatar(event.uri)
            }

            ProfileEvents.ReadProfileFromLocal -> readUserProfile()
        }
    }

    private fun uploadAvatar(uri: Uri) {
        viewModelScope.launch(dispatchersProvider.io) {
            uploadAvatarUseCase(uri = uri).collect { response ->
                when (response) {
                    is NetworkResponse.Error -> {
                        _errorMutSharedFlow.emit(response.errorMsg)
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
        viewModelScope.launch(dispatchersProvider.io) {
            _imageUriMutableStateFlow.value = fileUtil.createImageFile()
        }
    }

    private fun getProfile() {
        viewModelScope.launch(dispatchersProvider.io) {
            getUserUseCase()
                .catch { throwable ->
                    _errorMutSharedFlow.emit(throwable.localizedMessage)
                    _userProfileMutableState.update {
                        it.copy(profileUiState = ProfileUiState.Error(throwable.localizedMessage))
                    }
                }
                .collect { response ->
                    when (response) {
                        is NetworkResponse.Error -> {
                            _errorMutSharedFlow.emit(response.errorMsg)
                            _userProfileMutableState.update {
                                it.copy(profileUiState = ProfileUiState.Error(response.errorMsg))
                            }
                        }

                        is NetworkResponse.Loading -> {
                            _userProfileMutableState.update {
                                it.copy(profileUiState = ProfileUiState.Loading)
                            }
                        }

                        is NetworkResponse.Success -> {
                            _userProfileMutableState.update {
                                val user = it.user?.copy(
                                    email = response.data.email,
                                    avatarUrl = response.data.avatarUrl,
                                ) ?: User(
                                    "",
                                    response.data.email,
                                    "",
                                    response.data.avatarUrl
                                )
                                it.copy(user = user, profileUiState = ProfileUiState.Success)
                            }
                        }
                    }
                }
        }
    }

    private fun readUserProfile() {
        readUserUseCase()
            .flowOn(dispatchersProvider.io)
            .onEach { user ->
                _userProfileMutableState.update {
                    it.copy(
                        user = user,
                        profileUiState = ProfileUiState.Success
                    )
                }
            }.launchIn(viewModelScope)
    }

}