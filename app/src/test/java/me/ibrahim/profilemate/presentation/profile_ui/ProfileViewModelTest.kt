package me.ibrahim.profilemate.presentation.profile_ui

import android.net.Uri
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import me.ibrahim.profilemate.TestDispatchersProvider
import me.ibrahim.profilemate.data.dto.UserProfileResponse
import me.ibrahim.profilemate.data.remote.NetworkResponse
import me.ibrahim.profilemate.domain.use_cases.profile.GetUserUseCase
import me.ibrahim.profilemate.domain.use_cases.profile.ReadUserUseCase
import me.ibrahim.profilemate.domain.use_cases.profile.SaveUserUseCase
import me.ibrahim.profilemate.domain.use_cases.profile.UploadAvatarUseCase
import me.ibrahim.profilemate.domain.utils.DispatchersProvider
import me.ibrahim.profilemate.utils.FileUtil
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProfileViewModelTest {

    private var profileViewModel: ProfileViewModel? = null
    private lateinit var dispatchersProvider: DispatchersProvider

    @Before
    fun setUp() {
        dispatchersProvider = TestDispatchersProvider()
    }

    @After
    fun tearDown() {
        profileViewModel = null
    }


    @Test
    fun `test Profile Pic Uri before file creation, and Uri should be null`() {

        profileViewModel = ProfileViewModel(
            getUserUseCase = mockk<GetUserUseCase>(),
            readUserUseCase = mockk<ReadUserUseCase>(),
            saveUserUseCase = mockk<SaveUserUseCase>(),
            uploadAvatarUseCase = mockk<UploadAvatarUseCase>(),
            fileUtil = mockk<FileUtil>(relaxed = true),
            dispatchersProvider = dispatchersProvider
        )

        assertEquals(null, profileViewModel?.imageUriStateFlow?.value)
    }

    @Test
    fun `test File creation and Uri is returned`() = runTest {

        val fileUtil = mockk<FileUtil>(relaxed = true)

        mockkStatic(Uri::class)
        val expectedUri = mockk<Uri>()

        profileViewModel = ProfileViewModel(
            getUserUseCase = mockk<GetUserUseCase>(),
            readUserUseCase = mockk<ReadUserUseCase>(),
            saveUserUseCase = mockk<SaveUserUseCase>(),
            uploadAvatarUseCase = mockk<UploadAvatarUseCase>(),
            fileUtil = fileUtil,
            dispatchersProvider = dispatchersProvider
        )

        coEvery { fileUtil.createImageFile() } returns expectedUri

        profileViewModel?.onEvent(ProfileEvents.CreateImageFile)

        profileViewModel?.imageUriStateFlow?.test {
            val result = awaitItem()
            println(result)
            assertEquals(expectedUri, result)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onEvent triggered with GetProfile, update ProfileState`() = runTest {

        val getUserUseCase = mockk<GetUserUseCase>(relaxed = true)

        profileViewModel = ProfileViewModel(
            getUserUseCase = getUserUseCase,
            readUserUseCase = mockk<ReadUserUseCase>(),
            saveUserUseCase = mockk<SaveUserUseCase>(),
            uploadAvatarUseCase = mockk<UploadAvatarUseCase>(),
            fileUtil = mockk<FileUtil>(relaxed = true),
            dispatchersProvider = dispatchersProvider
        )

        val response = UserProfileResponse("avatar-url", "test@email")

        coEvery { getUserUseCase.invoke() } returns flow {
            emit(NetworkResponse.Success(response))
        }

        profileViewModel?.onEvent(ProfileEvents.GetProfileFromRemote)

        advanceUntilIdle()
        coVerify { getUserUseCase.invoke() }

        profileViewModel?.userProfileState?.test {
            val result = awaitItem()
            assertEquals(ProfileUiState.Success, result.profileUiState)
            cancelAndIgnoreRemainingEvents()
        }
    }


}