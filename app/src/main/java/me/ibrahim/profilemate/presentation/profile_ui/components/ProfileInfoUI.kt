package me.ibrahim.profilemate.presentation.profile_ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.ibrahim.profilemate.domain.models.User

@Composable
fun ProfileInfoUI(user: User?) {
    Column {
        Spacer(modifier = Modifier.padding(vertical = 25.dp))
        Text(text = "Email: ${user?.email}")
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Password: ${user?.password}")
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "User-ID: ${user?.userId}")
        Spacer(modifier = Modifier.height(12.dp))
    }
}