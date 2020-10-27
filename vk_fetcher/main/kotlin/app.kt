import client.AsyncVkClient
import client.VkClient
import utils.timestamps
import java.time.Instant

class App(private val client: VkClient) {

    suspend fun requestPostCountsOnce(hashTag: String, moment: Instant, hoursBefore: Long): List<Long> {
        return moment.timestamps(hoursBefore).map { window ->
            client.requestPostsCount(hashTag, window.first, window.second)
        }.map { response ->
            response.postsCount
        }.also {
            client.close()
        }
    }

}