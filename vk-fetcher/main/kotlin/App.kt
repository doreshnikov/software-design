import client.AsyncVkClient
import client.VkApiConfig
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

    suspend fun run(hashTag: String, hoursBefore: Long) {
        print("Last $hoursBefore hours search results for '#$hashTag':\n")
        val counts = requestPostCountsOnce(hashTag, Instant.now(), hoursBefore)

        counts.forEachIndexed { i, count ->
            val hours = (i - hoursBefore).toString().padStart(3)
            print("$hours: $count\n")
        }

        println("Total count: ${counts.sum()}")
    }

}