package me.ibrahim.profilemate.presentation.profile_ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.ibrahim.profilemate.presentation.profile_ui.ProfileState
import me.ibrahim.profilemate.presentation.profile_ui.ProfileUiState


@Composable
fun ProfileScreenContent(userProfileState: ProfileState, changeAvatar: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.padding(top = 25.dp))

        ProfileAvatar(
            avatarState = userProfileState.avatarState,
            avatarUrl = userProfileState.user?.avatarUrl,
            changeAvatar = changeAvatar
        )

        when (userProfileState.profileUiState) {
            is ProfileUiState.Error -> {
                ProfileErrorUI(userProfileState.profileUiState)
            }

            ProfileUiState.Loading -> {
                ProfileLoading()
            }

            ProfileUiState.Success -> {
                ProfileInfoUI(userProfileState.user)
            }

            null -> {}
        }
    }
}