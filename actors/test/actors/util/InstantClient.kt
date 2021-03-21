package actors.util

import clients.SearchClient
import clients.SearchRequest
import search.SearchEngine
import search.SearchItem
import search.SearchResult

class InstantClient(
    val engine: SearchEngine
) : SearchClient {

    override fun requestTop(request: SearchRequest): SearchResult {
        return SearchResult(
            engine,
            MutableList(request.count) { i -> SearchItem("//", "${request.query}$i", "description") }
        )
    }

}