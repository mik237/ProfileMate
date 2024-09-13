package me.ibrahim.profilemate.presentation.login_ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.ibrahim.profilemate.R
import me.ibrahim.profilemate.presentation.login_ui.LoginStates

@Composable
fun LoginButton(
    provideLoginState: () -> LoginStates,
    provideEmail: () -> String,
    providePassword: () -> String,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp)
            .height(50.dp),
        enabled = (provideLoginState() !is LoginStates.Loading) && (provideEmail().isNotEmpty() && providePassword().isNotEmpty()),
        onClick = onClick,
        shape = RoundedCornerShape(50)
    ) {
        Text(text = stringResource(id = R.string.login))
    }
}