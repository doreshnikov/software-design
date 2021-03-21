package clients

import search.SearchEngine

interface SearchClientFactory {

    fun getClient(engine: SearchEngine): SearchClient

}