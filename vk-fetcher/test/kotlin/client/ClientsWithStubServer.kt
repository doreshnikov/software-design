package client

import com.xebialabs.restito.builder.stub.StubHttp.whenHttp
import com.xebialabs.restito.semantics.Action.status
import com.xebialabs.restito.semantics.Action.stringContent
import com.xebialabs.restito.semantics.Condition.*
import io.ktor.client.features.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.glassfish.grizzly.http.Method
import org.glassfish.grizzly.http.util.HttpStatus
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.time.Instant

object ClientsWithStubServer {

    private const val PORT = 13337
    private lateinit var client: AsyncVkClient

    @JvmStatic
    @BeforeAll
    fun startClient() {
        assertDoesNotThrow {
            lateinit var props: VkApiConfig
            assertDoesNotThrow {
                props = VkApiConfig(
                    "test/resources",
                    "stub.properties",
                    "test.properties"
                )
            }
            client = AsyncVkClient(props)
        }
    }

    @JvmStatic
    @AfterAll
    fun tearDown() {
        client.close()
    }

    @Test
    fun testPrimitive() = runBlocking {
        withStubServer(PORT) {
            whenHttp(it)
                .match(method(Method.GET), startsWithUri("/ping"))
                .then(stringContent("pong"))

            val response = client.get {
                protocol = URLProtocol.HTTP
                host = "localhost"
                port = PORT
                path("ping")
            }
            assertEquals(response, "pong")
        }
    }

    @Test
    fun testFailing() = runBlocking {
        withStubServer(PORT) {
            whenHttp(it)
                .match(method(Method.GET), startsWithUri("/nothing"))
                .then(status(HttpStatus.NOT_FOUND_404))

            try {
                client.get {
                    protocol = URLProtocol.HTTP
                    host = "localhost"
                    port = PORT
                    path("nothing")
                }
                fail("Did not fail on no answer")
            } catch (e : ClientRequestException) {
            }
        }
    }

    @Test
    fun testActualData() = runBlocking {
        withStubServer(PORT) {
            whenHttp(it)
                .match(method(Method.GET), startsWithUri("/method/newsfeed.search"))
                .then(
                    stringContent("""{
                        "response": {
                            "items": [],
                            "count": 100,
                            "total_count": 100
                        }
                    }""".trimIndent())
                )

            val response = client.requestPostsCount("", Instant.now(), Instant.now())
            assertEquals(response.postsCount, 100)
        }
    }

}