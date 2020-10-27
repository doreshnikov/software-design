package client

import App
import io.ktor.client.*
import kotlinx.coroutines.runBlocking
import models.VkResponse
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import utils.beforeHours
import utils.toSeconds
import java.time.Instant
import kotlin.random.Random

object ClientMock {

    private lateinit var config: VkApiConfig

    @JvmStatic
    @BeforeAll
    fun setup() {
        assertDoesNotThrow {
            config = VkApiConfig("main/resources")
        }
    }

    private fun query(from: Instant, until: Instant) =
        "${config.protocol}://${config.host}:${config.port}/method/newsfeed.search" +
                "?q=%23hash" +
                "&v=${config.version.major}.${config.version.minor}" +
                "&access_token=${config.accessToken}" +
                "&count=0" +
                "&start_time=${from.toSeconds()}" +
                "&end_time=${until.toSeconds()}"


    /*@Test
    fun testMockClient() = runBlocking {
        val mock = Mockito.mock(AsyncHttpClient::class.java)
        val now = Instant.now()

        for (i in 1L..10L) {
            Mockito.`when`(
                mock.get(query(now.beforeHours(i), now))
            ).thenReturn("{\"response\":{\"total_count\":$i}}")
        }
        val client = object : AsyncHttpClient by mock, VkClient by AsyncVkClient(config) {
            override val client: HttpClient
                get() = mock.client

            override fun close() {
                client.close()
            }
        }

        for (i in 1L..10L) {
            assertEquals(client.requestPostsCount("hash", now, now.beforeHours(i)), VkResponse(i))
            print("${client.requestPostsCount("hash", now.beforeHours(i), now)}\n")
        }
    }*/

    @Test
    fun testMockApp() = runBlocking {
        val mock = Mockito.mock(VkClient::class.java)
        val expected = mutableListOf<Long>()
        val now = Instant.now()

        for (i in 10L downTo 0L) {
            val count = Random.nextLong(1, 1000)
            Mockito.`when`(
                mock.requestPostsCount("hash", now.beforeHours(i + 1), now.beforeHours(i))
            ).thenReturn(VkResponse(count))
            expected.add(count)
        }

        assertEquals(App(mock).requestPostCountsOnce("hash", now, 11), expected)
    }

}