package server

import com.beust.klaxon.Klaxon
import org.http4k.client.JavaHttpClient
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import search.SearchItem

class ServerTest {

    private val server = StubServer()
    private val client = JavaHttpClient()

    @BeforeEach
    fun setUp() {
        server.start()
    }

    @AfterEach
    fun tearDown() {
        server.stop()
    }

    @Test
    fun testResponds() {
        listOf(
            "/engine/request?c=5",
            "/engine/request",
            "/engine/request%20&c=invalid"
        ).forEach { path ->
            assertEquals(
                Status.OK,
                client(Request(Method.GET, "http://localhost:8080$path")).status
            )
        }
    }

    @Test
    fun testBadRequest() {
        assertEquals(
            Status.BAD_REQUEST,
            client(Request(Method.GET, "http://localhost:8080/favicon.ico")).status
        )
    }

    @Test
    fun testValidAmount() {
        listOf(1, 2, 3).forEach { c ->
            val list = client(Request(Method.GET, "http://localhost:8080/engine/request?c=$c"))
            assertEquals(c, Klaxon().parseArray<SearchItem>(list.bodyString())!!.size)
        }
    }

}