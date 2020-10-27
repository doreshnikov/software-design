package models

import kotlinx.serialization.*
import kotlinx.serialization.json.Json

@Serializable
data class VkResponse(
    @SerialName("total_count")
    val postsCount: Long = 0
) {

    companion object {
        @Serializable
        private data class ResponseWrapper(val response: VkResponse)

        fun parse(data: String) =
            Json { ignoreUnknownKeys = true }
                .decodeFromString<ResponseWrapper>(data)
                .response
    }

}