package clients

import com.beust.klaxon.Klaxon
import org.http4k.client.JavaHttpClient
import org.http4k.core.Method
import org.http4k.core.Request
import search.SearchEngine
import search.SearchItem
import search.SearchResult
import java.net.URLEncoder

class StubSearchClientFactory : SearchClientFactory {

    override fun getClient(engine: SearchEngine) = object : SearchClient {
        private val httpClient = JavaHttpClient()

        override fun requestTop(request: SearchRequest): SearchResult {
            val search = URLEncoder.encode(request.query, "utf-8")
            val response = httpClient(
                Request(
                    Method.GET,
                    "http://localhost:8080/${engine.name}/$search?c=${request.count}"
                )
            )
            val body = Klaxon().parseArray<SearchItem>(response.bodyString()) ?: return SearchResult.SearchFail(engine)
            return SearchResult(engine, body)
        }
    }

}