package me.ibrahim.profilemate.presentation.profile_ui

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ibrahim.profilemate.presentation.profile_ui.components.ImagePickerBottomSheet
import me.ibrahim.profilemate.presentation.profile_ui.components.ProfileScreenContent
import me.ibrahim.profilemate.utils.ImagePickerOption


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(profileVM: ProfileViewModel = hiltViewModel()) {

    val userProfileState by profileVM.userProfileState.collectAsStateWithLifecycle()

    val profilePicUri by profileVM.imageUriStateFlow.collectAsStateWithLifecycle()

    val cameraLauncher = rememberCameraLauncher(profilePicUri, profileVM)

    val galleryLauncher = rememberGalleryLauncher(profileVM)

    val cameraPermissionState = rememberCameraPermissionState(profilePicUri, cameraLauncher)

    var showBottomSheet by remember { mutableStateOf(false) }


    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        profileVM.onEvent(ProfileEvents.GetProfileFromRemote)
        profileVM.onEvent(ProfileEvents.ReadProfileFromLocal)

        profileVM.errorSharedFlow.collectLatest { errorMsg ->
            errorMsg?.let { err ->
                Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ProfileScreenContent(
            provideUserProfileState = { userProfileState },
            changeAvatar = { showBottomSheet = showBottomSheet.not() }
        )

    }

    if (showBottomSheet) {
        ImagePickerBottomSheet(onOptionSelected = { imagePickerOption ->
            handleImagePickerOptions(
                imagePickerOption,
                profileVM,
                cameraPermissionState,
                cameraLauncher,
                galleryLauncher,
                scope
            ) { showBottomSheet = false }
        })
    }
}

@OptIn(ExperimentalPermissionsApi::class)
fun handleImagePickerOptions(
    imagePickerOption: ImagePickerOption?,
    profileVM: ProfileViewModel,
    cameraPermissionState: PermissionState,
    cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>,
    scope: CoroutineScope,
    hideBottomSheet: () -> Unit
) {
    when (imagePickerOption) {
        ImagePickerOption.CAMERA -> {
            scope.launch {
                profileVM.onEvent(ProfileEvents.CreateImageFile)
                if (cameraPermissionState.status.isGranted.not()) {
                    cameraPermissionState.launchPermissionRequest()
                } else {
                    profileVM.imageUriStateFlow.value?.let { cameraLauncher.launch(it) }
                }
            }.invokeOnCompletion { hideBottomSheet() }
        }

        ImagePickerOption.GALLERY -> {
            scope.launch { galleryLauncher.launch("image/*") }.invokeOnCompletion {
                hideBottomSheet()
            }
        }

        else -> hideBottomSheet()
    }
}

@Composable
fun rememberCameraLauncher(profilePicUri: Uri?, profileVM: ProfileViewModel): ManagedActivityResultLauncher<Uri, Boolean> {
    return rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            profilePicUri?.let { profileVM.onEvent(ProfileEvents.UploadAvatar(it)) }
        }
    }
}

@Composable
fun rememberGalleryLauncher(profileVM: ProfileViewModel): ManagedActivityResultLauncher<String, Uri?> {
    return rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { profileVM.onEvent(ProfileEvents.UploadAvatar(it)) }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberCameraPermissionState(profilePicUri: Uri?, cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>): PermissionState {
    return rememberPermissionState(Manifest.permission.CAMERA) { granted ->
        if (granted) {
            profilePicUri?.let { cameraLauncher.launch(it) }
        }
    }
}
