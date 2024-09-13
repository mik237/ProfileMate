package me.ibrahim.profilemate.presentation.login_ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import me.ibrahim.profilemate.R

@Composable
fun PasswordInputField(
    providePassword: () -> String,
    onPasswordChanged: (String) -> Unit,
    onKeyBoard: KeyboardActionScope.() -> Unit
) {

    var showPassword by remember { mutableStateOf(false) }

    OutlinedTextField(value = providePassword(),
        onValueChange = onPasswordChanged,
        label = { Text(text = stringResource(id = R.string.password)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done, keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(onDone = onKeyBoard),
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {

            val image = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (showPassword) stringResource(id = R.string.hide_password) else stringResource(id = R.string.show_password)

            IconButton(onClick = { showPassword = showPassword.not() }) {
                Icon(imageVector = image, contentDescription = description)
            }
        })
}