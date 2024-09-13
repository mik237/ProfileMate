package me.ibrahim.profilemate.presentation.login_ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import me.ibrahim.profilemate.R

@Composable
fun EmailInputField(
    provideEmail: () -> String,
    onEmailChange: (String) -> Unit
) {
    OutlinedTextField(
        value = provideEmail(),
        onValueChange = onEmailChange,
        label = { Text(text = stringResource(id = R.string.email)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next
        ),
        shape = RoundedCornerShape(8.dp),
    )
}