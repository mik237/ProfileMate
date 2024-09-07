package me.ibrahim.profilemate.presentation.profile_ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.ibrahim.profilemate.presentation.profile_ui.ProfileUiState

@Composable
fun ProfileErrorUI(uiState: ProfileUiState?) {
    val errorMsg = (uiState as ProfileUiState.Error).errorMsg
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp, vertical = 50.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$errorMsg",
            fontSize = 25.sp,
            color = MaterialTheme.colorScheme.error
        )
    }
}