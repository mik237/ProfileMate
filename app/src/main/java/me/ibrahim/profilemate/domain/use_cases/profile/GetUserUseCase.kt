package me.ibrahim.profilemate.domain.use_cases.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import me.ibrahim.profilemate.data.remote.NetworkResponse
import me.ibrahim.profilemate.presentation.profile_ui.AvatarState
import me.ibrahim.profilemate.presentation.profile_ui.ProfileState
import me.ibrahim.profilemate.presentation.profile_ui.ProfileUiState
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    val readRemoteUserUseCase: ReadRemoteUserUseCase,
    val readLocalUserUseCase: ReadLocalUserUseCase
) {
    operator fun invoke(): Flow<ProfileState> = flow {

        var profileState = ProfileState()

        readRemoteUserUseCase()
            .catch { throwable ->
                profileState = profileState.copy(profileUiState = ProfileUiState.Error(throwable.localizedMessage))
                emit(profileState)
            }
            .collect { response ->

                val user = readLocalUserUseCase().firstOrNull()

                when (response) {
                    is NetworkResponse.Loading -> {
                        profileState = profileState.copy(profileUiState = ProfileUiState.Loading)
                    }

                    else -> {
                        val errorMsg = if (response is NetworkResponse.Error) response.errorMsg else "User Not Found"
                        profileState = profileState.copy(
                            user = user,
                            avatarState = if (user?.avatarUrl?.isEmpty() == true) {
                                AvatarState.None
                            } else {
                                AvatarState.UploadSuccess(user?.avatarUrl ?: "")
                            },
                            profileUiState = if (user != null) ProfileUiState.Success else ProfileUiState.Error(errorMsg = errorMsg)
                        )
                    }
                }
                emit(profileState)
            }
    }
}