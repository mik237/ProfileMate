package me.ibrahim.profilemate.presentation.profile_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileScreen(profileVM: ProfileViewModel = hiltViewModel()) {

    val userProfile by profileVM.userProfileStateFlow.collectAsStateWithLifecycle()

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = "This is profile screen")
            Spacer(modifier = Modifier.height(22.dp))

            Text(text = "Email: ${userProfile?.email}")
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Password: ${userProfile?.password}")
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "User-ID: ${userProfile?.userId}")
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}