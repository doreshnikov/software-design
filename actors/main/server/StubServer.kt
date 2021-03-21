package server

import com.beust.klaxon.Klaxon
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.server.ApacheServer
import org.http4k.server.asServer
import search.SearchItem
import java.net.URLDecoder

class StubServer {

    private val server = this::processRequest.asServer(ApacheServer(8080))

    private fun processRequest(request: Request): Response {
        val pathItems = request.uri.path.split("/")
        if (pathItems.size < 3) {
            return Response(Status.BAD_REQUEST)
        }

        val engine = pathItems[1]
        val search = URLDecoder.decode(pathItems[2], "utf-8")
        var count = 5
        try {
            count = (request.query("c")!!).toInt()
        } catch (e: Exception) {
        }

        val words = search.split(" ").toMutableList()
        for (i in 1..(count - words.size)) {
            words.add("default$i")
        }
        val items = words.take(count).map { SearchItem(
            "https://$it.com/",
            "Example title: $it",
            "Example preview for search engine '$engine': $it"
        ) }

        return Response(Status.OK).body(Klaxon().toJsonString(items))
    }

    fun start() {
        server.start()
    }

    fun stop() {
        server.stop()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            StubServer().start()
        }
    }

}