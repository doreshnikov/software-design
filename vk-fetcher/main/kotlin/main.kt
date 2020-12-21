import client.AsyncVkClient
import client.VkApiConfig

suspend fun main(args: Array<String>) {

    require(args.size == 2) { "Please specify a <hashtag> and <hours>" }

    val hashTag = args[0]
    require(hashTag.matches(Regex("[\\w]+"))) { "Hashtag should only contain letters, digits or underscores" }
    val hoursBefore = args[1].toLongOrNull()?.also {
        require(it in 1..24) { "You can request stats only for last 24 hours" }
    } ?: error("Hours should be a valid number")

    val config = VkApiConfig("main/resources")
    val client = AsyncVkClient(config)

    App(client).run(hashTag, hoursBefore)

}