package me.ibrahim.profilemate.presentation.profile_ui

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun ProfileScreen(profileVM: ProfileViewModel = hiltViewModel()) {

    val userProfile by profileVM.userProfileStateFlow.collectAsStateWithLifecycle()


    var picUrl: String by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val context = LocalContext.current
        Spacer(modifier = Modifier.padding(top = 25.dp))
        ProfileImage(Uri.parse(picUrl)) {
            ///TODO: by usin it (the local file path for image) update the profile image
        }


        Spacer(modifier = Modifier.padding(vertical = 25.dp))
        Text(text = "Email: ${userProfile?.email}")
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Password: ${userProfile?.password}")
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "User-ID: ${userProfile?.userId}")
        Spacer(modifier = Modifier.height(12.dp))

    }
}