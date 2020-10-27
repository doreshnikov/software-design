package client

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.http.*
import models.VkResponse
import utils.toSeconds
import java.time.Instant

class AsyncVkClient(private val config: VkApiConfig) : AsyncHttpClient, VkClient {

    override val client = HttpClient(Apache)

    override suspend fun requestPostsCount(hashTag: String, from: Instant, until: Instant): VkResponse {
        val method = "newsfeed.search"
        val query = URLBuilder().apply {
            protocol = URLProtocol.createOrDefault(config.protocol)
            host = config.host
            port = config.port
            path("method", method)
            parameters.apply {
                set("q", "#${hashTag}")
                set("v", "${config.version.major}.${config.version.minor}")
                set("access_token", config.accessToken ?: error("Access token is required for this method"))
                set("count", "0")
                set("start_time", from.toSeconds().toString())
                set("end_time", until.toSeconds().toString())
            }
        }.buildString()

        return VkResponse.parse(get(query))
    }

    override fun close() {
        client.close()
    }

}