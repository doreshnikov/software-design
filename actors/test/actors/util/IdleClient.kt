package actors.util

import clients.SearchClient
import clients.SearchRequest
import search.SearchResult

class IdleClient : SearchClient {

    override fun requestTop(request: SearchRequest): SearchResult {
        Thread.sleep(15000)
        throw RuntimeException("wait finished")
    }

}