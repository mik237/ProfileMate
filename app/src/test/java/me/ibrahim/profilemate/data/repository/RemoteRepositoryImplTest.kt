package me.ibrahim.profilemate.data.repository

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import me.ibrahim.profilemate.data.dto.LoginRequest
import me.ibrahim.profilemate.data.dto.LoginResponse
import me.ibrahim.profilemate.data.dto.UserProfileResponse
import me.ibrahim.profilemate.data.remote.NetworkResponse
import me.ibrahim.profilemate.data.remote.RemoteAPIs
import me.ibrahim.profilemate.domain.managers.ApiManager
import me.ibrahim.profilemate.domain.managers.ConnectionManager
import me.ibrahim.profilemate.domain.managers.SessionManager
import me.ibrahim.profilemate.domain.repository.RemoteRepository
import me.ibrahim.profilemate.utils.FileUtil
import me.ibrahim.profilemate.utils.HttpCodes
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteRepositoryImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var remoteRepository: RemoteRepository
    private lateinit var remoteAPIs: RemoteAPIs

    private lateinit var apiManager: ApiManager
    private lateinit var connectionManager: ConnectionManager
    private lateinit var fileUtil: FileUtil
    private lateinit var sessionManager: SessionManager

    @Before
    fun setUp() {

        apiManager = mockk(relaxed = true)
        connectionManager = mockk(relaxed = true)
        fileUtil = mockk(relaxed = true)
        sessionManager = mockk(relaxed = true)

        mockWebServer = MockWebServer()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        remoteAPIs = retrofit.create(RemoteAPIs::class.java)
        remoteRepository = RemoteRepositoryImpl(
            remoteAPIs, apiManager, connectionManager, sessionManager, fileUtil
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        clearAllMocks()
    }


    @Test
    fun `login should return success response`() = runTest {

        val loginRequest = LoginRequest(email = "test@example.com", password = "password")

        every { connectionManager.isConnected() } returns true

        coEvery { apiManager.handleApi(any<suspend () -> Response<LoginResponse>>()) } coAnswers {
            val call = args[0] as suspend () -> Response<LoginResponse>
            val response = call.invoke()
            if (response.isSuccessful) {
                NetworkResponse.Success(response.body()!!)
            } else {
                NetworkResponse.Error("Error")
            }
        }


        val mockResponse = MockResponse().setResponseCode(200).setBody(
            """
                {
                    "userid": "mock-user-id",
                    "token": "mock-token"
                }
                """.trimIndent()
        )

        mockWebServer.enqueue(mockResponse)

        val result = remoteRepository.login(loginRequest).toList()

        println(result[0])
        assertTrue(result[0] is NetworkResponse.Loading)
        println(result[1])
        assertTrue(result[1] is NetworkResponse.Success)

        assertEquals("mock-user-id", (result[1] as NetworkResponse.Success).data.userid)
        assertEquals("mock-token", (result[1] as NetworkResponse.Success).data.token)
    }

    @Test
    fun `login should return error for invalid credentials`() = runTest {

        val loginRequest = LoginRequest(email = "", password = "")

        every { connectionManager.isConnected() } returns true

        coEvery { apiManager.handleApi(any<suspend () -> Response<LoginResponse>>()) } coAnswers {
            val call = args[0] as suspend () -> Response<LoginResponse>
            val response = call.invoke()
            if (response.isSuccessful) {
                NetworkResponse.Success(response.body()!!)
            } else {
                NetworkResponse.Error("Email or password cannot be empty", errorCode = response.code())
            }
        }


        val mockResponse = MockResponse().setResponseCode(401).setBody(
            """
                {
                    "errorMsg": "Email or password cannot be empty",
                    "errorCode": 401
                }
                """.trimIndent()
        )

        mockWebServer.enqueue(mockResponse)

        val result = remoteRepository.login(loginRequest).toList()

        println(result[0])
        assertTrue(result[0] is NetworkResponse.Loading)
        println(result[1])
        assertTrue(result[1] is NetworkResponse.Error)
    }

    @Test
    fun `login should return 'Network Connection Error' if internet is not connected`() = runTest {

        val loginRequest = LoginRequest(email = "test@example.com", password = "password")

        every { connectionManager.isConnected() } returns false

        coEvery { apiManager.handleApi(any<suspend () -> Response<LoginResponse>>()) } coAnswers {
            val call = args[0] as suspend () -> Response<LoginResponse>
            val response = call.invoke()
            if (response.isSuccessful) {
                NetworkResponse.Success(response.body()!!)
            } else {
                NetworkResponse.Error("Error")
            }
        }


        val mockResponse = MockResponse().setResponseCode(200).setBody(
            """
                {
                    "userid": "mock-user-id",
                    "token": "mock-token"
                }
                """.trimIndent()
        )

        mockWebServer.enqueue(mockResponse)

        val result = remoteRepository.login(loginRequest).toList()

        println(result[0])
        assertTrue(result[0] is NetworkResponse.Loading)
        println(result[1])
        assertTrue(result[1] is NetworkResponse.Error)

        assertEquals("Network Connection Error!", (result[1] as NetworkResponse.Error).errorMsg)
        assertEquals(500, (result[1] as NetworkResponse.Error).errorCode)
    }


    @Test
    fun `getUser should return 'Session Expire' if token is expired`() = runTest {

        every { connectionManager.isConnected() } returns true
        coEvery { sessionManager.isActiveSession() } returns false

        coEvery { apiManager.handleApi(any<suspend () -> Response<UserProfileResponse>>()) } coAnswers {
            val call = args[0] as suspend () -> Response<UserProfileResponse>
            val response = call.invoke()
            if (response.isSuccessful) {
                NetworkResponse.Success(response.body()!!)
            } else {
                NetworkResponse.Error("Error")
            }
        }

        val mockResponse = MockResponse().setResponseCode(HttpCodes.SESSION_EXPIRED.code).setBody(
            """
                {
                    "email": "test@example.com",
                    "avatar_url": ""
                }
                """.trimIndent()
        )

        mockWebServer.enqueue(mockResponse)

        val result = remoteRepository.getUser("123").toList()

        println(result[0])
        assertTrue(result[0] is NetworkResponse.Loading)
        println(result[1])
        assertTrue(result[1] is NetworkResponse.Error)

        assertEquals("Session Expired!", (result[1] as NetworkResponse.Error).errorMsg)
        assertEquals(419, (result[1] as NetworkResponse.Error).errorCode)
    }
}