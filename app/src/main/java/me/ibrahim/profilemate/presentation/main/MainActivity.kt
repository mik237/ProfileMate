package me.ibrahim.profilemate.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import dagger.hilt.android.AndroidEntryPoint
import me.ibrahim.profilemate.R
import me.ibrahim.profilemate.presentation.login_ui.LoginScreen
import me.ibrahim.profilemate.presentation.navigation.NavGraph
import me.ibrahim.profilemate.presentation.navigation.Routes
import me.ibrahim.profilemate.ui.theme.ProfileMateTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen().apply {
            setKeepOnScreenCondition { mainViewModel.splashScreenHold }
        }

        setContent {
            ProfileMateTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = {
                            Text(
                                text = stringResource(id = R.string.app_name),
                                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
                            )
                        })
                    }) { innerPadding ->

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        val startDest = mainViewModel.startDestination
                        NavGraph(startDestination = startDest)
                    }
                }
            }
        }
    }
}