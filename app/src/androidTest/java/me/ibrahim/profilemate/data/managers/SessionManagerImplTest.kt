package me.ibrahim.profilemate.data.managers

import android.util.Base64
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID


class SessionManagerImplTest {

    private var localDataStoreManager: LocalDataStoreManager = mockk(relaxed = true)
    private var sessionManager = SessionManagerImpl(localDataStoreManager)


    @Test
    fun test_isActiveSession_token_valid_returns_true() = runBlocking {

        val expiryTime = System.currentTimeMillis() + (10 * 60 * 1000) //10 minutes
        val uniqueId = UUID.randomUUID().toString()
        val token = "$expiryTime:$uniqueId"

        coEvery { localDataStoreManager.getToken() } answers {
            Base64.encodeToString(token.toByteArray(), Base64.NO_WRAP)
        }


        val result = sessionManager.isActiveSession()

        assertTrue(result)
    }

    @Test
    fun test_isActiveSession_token_expired_returns_false() = runBlocking {

        val expiryTime = System.currentTimeMillis() - (10 * 60 * 1000) // 10 minutes passed
        val uniqueId = UUID.randomUUID().toString()
        val token = "$expiryTime:$uniqueId"

        coEvery { localDataStoreManager.getToken() } answers {
            Base64.encodeToString(token.toByteArray(), Base64.NO_WRAP)
        }

        val result = sessionManager.isActiveSession()

        assertFalse(result)
    }

    @Test
    fun test_isActiveSession_token_null_returns_false() = runBlocking {

        coEvery { localDataStoreManager.getToken() } returns null

        val result = sessionManager.isActiveSession()

        assertFalse(result)
    }


    @Test
    fun test_isActiveSession_token_invalid_format_returns_false() = runBlocking {

        val token = "invalid-token-format"

        coEvery { localDataStoreManager.getToken() } answers {
            Base64.encodeToString(token.toByteArray(), Base64.NO_WRAP)
        }

        val result = sessionManager.isActiveSession()

        assertFalse(result)
    }
}