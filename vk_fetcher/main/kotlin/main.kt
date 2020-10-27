import client.VkApiConfig
import client.AsyncVkClient
import java.time.Instant

suspend fun main(args: Array<String>) {

    require(args.size == 2) { "Please specify a <hashtag> and <hours>" }

    val hashTag = args[0]
    require(hashTag.matches(Regex("[\\w]+"))) { "Hashtag should only contain letters, digits or underscores" }
    val hoursBefore = args[1].toLongOrNull()?.also {
        require(it in 1..24) { "You can request stats only for last 24 hours" }
    } ?: error("Hours should be a valid number")

    val config = VkApiConfig("main/resources")
    val client = AsyncVkClient(config)

    print("Last $hoursBefore hours search results for '#$hashTag':\n")
    val counts = App(client).requestPostCountsOnce(hashTag, Instant.now(), hoursBefore)

    counts.forEachIndexed { i, count ->
        val hours = (i - hoursBefore).toString().padStart(3)
        print("$hours: $count\n")
    }

    println("Total count: ${counts.sum()}")

}