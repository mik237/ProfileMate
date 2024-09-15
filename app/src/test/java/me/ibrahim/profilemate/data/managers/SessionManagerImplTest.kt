package me.ibrahim.profilemate.data.managers


import android.util.Base64
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.UUID

class SessionManagerImplTest {

    private var localDataStoreManager: LocalDataStoreManager = mockk(relaxed = true)
    private var sessionManager = SessionManagerImpl(localDataStoreManager)

    @Before
    fun setUp() {
        mockkStatic(Base64::class)
    }


    @Test
    fun `test isActiveSession token valid returns true`() = runBlocking {

        val expiryTime = System.currentTimeMillis() + (10 * 60 * 1000) //10 minutes
        val uniqueId = UUID.randomUUID().toString()
        val token = "$expiryTime:$uniqueId"

        every { Base64.encodeToString(token.toByteArray(), Base64.NO_WRAP) } answers {
            java.util.Base64.getEncoder().encodeToString(token.toByteArray())
        }

        val stringSlot = slot<String>()

        every { Base64.decode(capture(stringSlot), Base64.NO_WRAP) } answers {
            java.util.Base64.getDecoder().decode(stringSlot.captured)
        }

        coEvery { localDataStoreManager.getToken() } answers {
            java.util.Base64.getEncoder().encodeToString(token.toByteArray())
        }


        val result = sessionManager.isActiveSession()

        assertTrue(result)
    }


    @Test
    fun `test isActiveSession token expired returns false`() = runBlocking {

        val expiryTime = System.currentTimeMillis() - (10 * 60 * 1000) // 10 minutes passed
        val uniqueId = UUID.randomUUID().toString()
        val token = "$expiryTime:$uniqueId"

        every { Base64.encodeToString(token.toByteArray(), Base64.NO_WRAP) } answers {
            java.util.Base64.getEncoder().encodeToString(token.toByteArray())
        }

        val stringSlot = slot<String>()

        every { Base64.decode(capture(stringSlot), Base64.NO_WRAP) } answers {
            java.util.Base64.getDecoder().decode(stringSlot.captured)
        }

        coEvery { localDataStoreManager.getToken() } answers {
            java.util.Base64.getEncoder().encodeToString(token.toByteArray())
        }


        val result = sessionManager.isActiveSession()

        assertFalse(result)
    }

    @Test
    fun `test isActiveSession token null returns false`() = runBlocking {

        coEvery { localDataStoreManager.getToken() } returns null

        val result = sessionManager.isActiveSession()

        assertFalse(result)
    }

    @Test
    fun `test isActiveSession token invalid format returns false`() = runBlocking {

        val token = "invalid-token-format"

        /*every { Base64.encodeToString(token.toByteArray(), Base64.DEFAULT) } answers {
            java.util.Base64.getEncoder().encodeToString(token.toByteArray())
        }*/

        val stringSlot = slot<String>()

        every { Base64.decode(capture(stringSlot), Base64.NO_WRAP) } answers {
            java.util.Base64.getDecoder().decode(stringSlot.captured)
        }

        coEvery { localDataStoreManager.getToken() } answers {
            java.util.Base64.getEncoder().encodeToString(token.toByteArray())
        }


        val result = sessionManager.isActiveSession()

        assertFalse(result)
    }
}