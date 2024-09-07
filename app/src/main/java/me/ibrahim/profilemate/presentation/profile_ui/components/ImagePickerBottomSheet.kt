package me.ibrahim.profilemate.presentation.profile_ui.components

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import kotlinx.coroutines.launch
import me.ibrahim.profilemate.R
import me.ibrahim.profilemate.utils.ImagePicker
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerBottomSheet(
    onOptionSelected: (ImagePicker?) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { onOptionSelected(null) },
        sheetState = bottomSheetState
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = {
                    scope.launch {
                        bottomSheetState.hide()
                        onOptionSelected(ImagePicker.GALLERY)
                    }
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_gallery),
                        contentDescription = "Gallery",
                        modifier = Modifier.size(150.dp)
                    )
                }
                IconButton(onClick = {
                    scope.launch {
                        bottomSheetState.hide()
                        onOptionSelected(ImagePicker.CAMERA)
                    }
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_camera),
                        contentDescription = "Camera",
                        modifier = Modifier
                            .size(150.dp)
                    )
                }

            }
        }
    }
}