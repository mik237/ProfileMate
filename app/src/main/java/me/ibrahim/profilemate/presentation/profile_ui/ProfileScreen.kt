package me.ibrahim.profilemate.presentation.profile_ui

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ibrahim.profilemate.presentation.profile_ui.components.ImagePickerBottomSheet
import me.ibrahim.profilemate.presentation.profile_ui.components.ProfileAvatar
import me.ibrahim.profilemate.presentation.profile_ui.components.ProfileErrorUI
import me.ibrahim.profilemate.presentation.profile_ui.components.ProfileInfoUI
import me.ibrahim.profilemate.presentation.profile_ui.components.ProfileLoading
import me.ibrahim.profilemate.utils.ImagePicker


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(profileVM: ProfileViewModel = hiltViewModel()) {

    val userProfileState by profileVM.userProfileState.collectAsStateWithLifecycle()

    val profilePicUri by profileVM.imageUriStateFlow.collectAsStateWithLifecycle()

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            profilePicUri?.let { profileVM.onEvent(ProfileEvents.UploadAvatar(it)) }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { profileVM.onEvent(ProfileEvents.UploadAvatar(it)) }
    }

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA) { granted ->
        if (granted) {
            profilePicUri?.let { cameraLauncher.launch(it) }
        }
    }

    var showBottomSheet by remember { mutableStateOf(false) }


    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        profileVM.onEvent(ProfileEvents.GetProfile)

        profileVM.errorSharedFlow.collectLatest { errorMsg ->
            errorMsg?.let { err ->
                Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.padding(top = 25.dp))

        ProfileAvatar(userProfileState.user?.avatarUrl) { showBottomSheet = showBottomSheet.not() }


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

    if (showBottomSheet) {
        ImagePickerBottomSheet(onOptionSelected = { imagePicker ->

            when (imagePicker) {
                ImagePicker.CAMERA -> {
                    scope.launch {
                        profileVM.onEvent(ProfileEvents.CreateImageFile)
                        if (cameraPermissionState.status.isGranted.not()) {
                            cameraPermissionState.launchPermissionRequest()
                        } else {
                            profilePicUri?.let { cameraLauncher.launch(it) }
                        }
                    }.invokeOnCompletion { showBottomSheet = false }
                }

                ImagePicker.GALLERY -> {
                    scope.launch { galleryLauncher.launch("image/*") }.invokeOnCompletion {
                        showBottomSheet = false
                    }
                }

                else -> {
                    showBottomSheet = false
                }
            }

        })
    }
}

