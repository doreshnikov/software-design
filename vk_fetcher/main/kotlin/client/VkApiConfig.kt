package client

import utils.TypedConfig

class VkApiConfig(
    propertiesDirectory: String,
    propertiesFilename: String = "application.properties",
    tokenFilename: String? = "local.properties"
) {

    data class Version(val major: Int, val minor: Int)

    val protocol: String
    val host: String
    val port: Int
    val version: Version

    init {
        val properties = TypedConfig("$propertiesDirectory/$propertiesFilename")
        protocol = properties.get("protocol")
        host = properties.get("host")
        port = properties.get("port")
        version = Version(
            properties.get("version.major"),
            properties.get("version.minor")
        )
    }

    val accessToken: String? = tokenFilename?.let {
        TypedConfig("$propertiesDirectory/$it")
            .get<String>("access_token")
    }

}