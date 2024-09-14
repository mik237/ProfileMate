package me.ibrahim.profilemate.presentation.profile_ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import me.ibrahim.profilemate.R
import me.ibrahim.profilemate.presentation.profile_ui.AvatarState

@Composable
fun ProfileAvatar(
    avatarState: AvatarState,
    avatarUrl: String?,
    changeAvatar: () -> Unit = {}
) {

    val context = LocalContext.current
    val placeholderImage = R.drawable.ic_placeholder
    val imageLoader = context.imageLoader

    val url by rememberUpdatedState(newValue = avatarUrl)

    val imageRequest = ImageRequest.Builder(context)
        .crossfade(400)
        .dispatcher(Dispatchers.IO)
        .data(url)
        .memoryCacheKey(url)
        .diskCacheKey(url)
        .placeholder(placeholderImage)
        .error(placeholderImage)
        .fallback(placeholderImage)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()


    Box(
        modifier = Modifier
            .size(140.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape),
        contentAlignment = Alignment.Center
    ) {


        AsyncImage(
            model = imageRequest,
            imageLoader = imageLoader,
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(id = R.string.profile_pic),
        )

        if (avatarState is AvatarState.Uploading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(138.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        if (avatarState !is AvatarState.Uploading) {
            IconButton(
                onClick = changeAvatar,
                modifier = Modifier
                    .size(35.dp)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .align(Alignment.BottomEnd),
                colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.primary),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = stringResource(id = R.string.edit_profile_pic),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}