package me.ibrahim.profilemate.presentation.profile_ui

import me.ibrahim.profilemate.domain.models.User

data class ProfileState(
    val user: User? = null,
    val avatarState: AvatarState = AvatarState.None,
    val profileUiState: ProfileUiState? = null
)

sealed class AvatarState {
    data object None : AvatarState()
    data object Uploading : AvatarState()
    data class UploadSuccess(val avatarUri: String) : AvatarState()
    data class UploadFailed(val errorMsg: String? = null) : AvatarState()
}

sealed class ProfileUiState {
    data object Loading : ProfileUiState()
    data object Success : ProfileUiState()
    data class Error(val errorMsg: String? = null) : ProfileUiState()
}