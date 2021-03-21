package clients

import search.SearchResult

interface SearchClient {

    fun requestTop(request: SearchRequest): SearchResult

}