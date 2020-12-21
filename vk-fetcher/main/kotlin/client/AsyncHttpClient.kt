package client

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.withTimeout

interface AsyncHttpClient {

    val client: HttpClient

    @Deprecated("use other get instead")
    suspend fun get(build: URLBuilder.() -> Unit): String {
        val query = URLBuilder().also(build).buildString()
        return get(query)
    }

    suspend fun get(query: String): String {
        return withTimeout(5000) {
            client.get(query)
        }
    }

    fun close() {
        client.close()
    }

}