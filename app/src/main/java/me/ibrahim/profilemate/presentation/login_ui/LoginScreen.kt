package me.ibrahim.profilemate.presentation.login_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.ibrahim.profilemate.R
import me.ibrahim.profilemate.presentation.login_ui.components.EmailInputField
import me.ibrahim.profilemate.presentation.login_ui.components.LoginButton
import me.ibrahim.profilemate.presentation.login_ui.components.PasswordInputField

@Composable
fun LoginScreen(loginVM: LoginViewModel = hiltViewModel()) {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

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
            text = stringResource(id = R.string.login),
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