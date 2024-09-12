package me.ibrahim.profilemate.presentation.login_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.ibrahim.profilemate.R

@Composable
fun LoginScreen(loginVM: LoginViewModel = hiltViewModel()) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginState by loginVM.loginStateFlow.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), fontSize = 40.sp
        )

        Spacer(modifier = Modifier.padding(20.dp))

        EmailInputField(
            provideEmail = { email },
            onEmailChange = { email = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        PasswordInputField(
            providePassword = { password },
            onPasswordChanged = { password = it },
            onKeyBoard = { focusManager.clearFocus() }
        )

        Spacer(modifier = Modifier.height(20.dp))

        LoginButton(
            provideLoginState = { loginState },
            provideEmail = { email },
            providePassword = { password },
            onClick = {
                focusManager.clearFocus()
                loginVM.onEvent(LoginScreenEvent.LoginClicked(email, password))
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (loginState is LoginStates.Error) {
            Text(
                text = "${stringResource(id = R.string.error)}: ${(loginState as LoginStates.Error).error}", color = MaterialTheme.colorScheme.error
            )
        }
        if (loginState is LoginStates.Loading) {
            CircularProgressIndicator()
        }
        if (loginState is LoginStates.Success) {
            loginVM.onEvent(LoginScreenEvent.SaveUser((loginState as LoginStates.Success).user))
            loginVM.onEvent(LoginScreenEvent.SaveToken((loginState as LoginStates.Success).token))
        }
    }
}

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