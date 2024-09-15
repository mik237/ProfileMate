package me.ibrahim.profilemate.presentation.login_ui


import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import me.ibrahim.profilemate.TestDispatchersProvider
import me.ibrahim.profilemate.data.dto.LoginRequest
import me.ibrahim.profilemate.data.dto.LoginResponse
import me.ibrahim.profilemate.data.remote.NetworkResponse
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import me.ibrahim.profilemate.domain.models.User
import me.ibrahim.profilemate.domain.use_cases.login.LoginUseCase
import me.ibrahim.profilemate.domain.use_cases.main.SaveTokenUseCase
import me.ibrahim.profilemate.domain.use_cases.profile.SaveUserUseCase
import me.ibrahim.profilemate.domain.utils.DispatchersProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class LoginViewModelTest {


    private lateinit var dispatchersProvider: DispatchersProvider


    @Before
    fun setUp() {
        dispatchersProvider = TestDispatchersProvider()
    }

    private fun getLoginViewModel(
        loginUseCase: LoginUseCase = mockk<LoginUseCase>(),
        saveTokenUseCase: SaveTokenUseCase = mockk<SaveTokenUseCase>(),
        saveUserUseCase: SaveUserUseCase = mockk<SaveUserUseCase>()
    ): LoginViewModel {
        val loginViewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            saveTokenUseCase = saveTokenUseCase,
            saveUserUseCase = saveUserUseCase,
            dispatchersProvider = dispatchersProvider
        )
        return loginViewModel
    }


    @Test
    fun `loginStateFlow initial state is LoginStates_Initial`() = runTest {
        val loginUseCase = mockk<LoginUseCase>()
        val saveTokenUseCase = mockk<SaveTokenUseCase>()
        val saveUserUseCase = mockk<SaveUserUseCase>()

        val loginViewModel = getLoginViewModel(loginUseCase, saveTokenUseCase, saveUserUseCase)

        loginViewModel.loginStateFlow.test {
            val state = awaitItem()
            assertEquals(LoginStates.Initial, state)
        }
    }

    @Test
    fun `onEvent triggered with LoginClicked and valid credentials results in Login Success`() = runTest {

        val loginUseCase = mockk<LoginUseCase>()
        val saveTokenUseCase = mockk<SaveTokenUseCase>(relaxed = true)
        val saveUserUseCase = mockk<SaveUserUseCase>(relaxed = true)
        val loginViewModel = getLoginViewModel(loginUseCase, saveTokenUseCase, saveUserUseCase)

        val testEmail = "test@email.com"
        val testPassword = "test_pas"

        val requestSlot = slot<LoginRequest>()
        val expectedResponse = LoginResponse("123", "token:abcdef")

        coEvery { loginUseCase(capture(requestSlot)) } returns flowOf(NetworkResponse.Success(expectedResponse))

        loginViewModel.onEvent(LoginScreenEvent.LoginClicked(testEmail, testPassword))

        coVerify { loginUseCase.invoke(any()) }

        val email = requestSlot.captured.email
        val pass = requestSlot.captured.password

        assertEquals(testEmail, email)
        assertEquals(testPassword, pass)

        loginViewModel.loginStateFlow.test {
            val item = awaitItem()
            println(item)
            assertTrue(item is LoginStates.Success)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent triggered with LoginClicked and invalid credentials results in Login Error`() = runTest {

        val loginUseCase = mockk<LoginUseCase>()
        val saveTokenUseCase = mockk<SaveTokenUseCase>()
        val saveUserUseCase = mockk<SaveUserUseCase>()

        val loginViewModel = getLoginViewModel(loginUseCase, saveTokenUseCase, saveUserUseCase)

        val testEmail = "invalid email"
        val testPassword = ""

        val expectedErrorMsg = "Invalid Credentials"
        val requestSlot = slot<LoginRequest>()
        coEvery { loginUseCase(capture(requestSlot)) } returns flowOf(NetworkResponse.Error(errorMsg = expectedErrorMsg))

        loginViewModel.onEvent(LoginScreenEvent.LoginClicked(testEmail, testPassword))

        coVerify { loginUseCase.invoke(any()) }
        loginViewModel.loginStateFlow.test {
            val resultItem = awaitItem()
            print(resultItem)
            assertTrue(resultItem is LoginStates.Error)
            assertEquals(expectedErrorMsg, (resultItem as LoginStates.Error).error)
        }
    }
}