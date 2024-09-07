package me.ibrahim.profilemate.presentation.profile_ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.ibrahim.profilemate.R
import me.ibrahim.profilemate.domain.models.User

@Composable
fun ProfileInfoUI(user: User?) {
    Column(modifier = Modifier.padding(25.dp)) {
        Spacer(modifier = Modifier.padding(25.dp))
        ProfileInfoItem(stringResource(id = R.string.email), "${user?.email}")
        Spacer(modifier = Modifier.height(12.dp))
        ProfileInfoItem(stringResource(id = R.string.password), "${user?.password}")
        Spacer(modifier = Modifier.height(12.dp))
        ProfileInfoItem(stringResource(id = R.string.userId), "${user?.userId}")
        Spacer(modifier = Modifier.height(12.dp))
    }
}
