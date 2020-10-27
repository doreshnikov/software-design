package client

import models.VkResponse
import java.time.Instant

interface VkClient {

    suspend fun requestPostsCount(hashTag: String, from: Instant, until: Instant): VkResponse

    fun close()

}