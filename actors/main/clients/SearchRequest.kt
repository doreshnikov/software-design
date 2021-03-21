package clients

data class SearchRequest(
    val query: String,
    val count: Int
)