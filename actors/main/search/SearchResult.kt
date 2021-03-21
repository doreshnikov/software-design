package search

data class SearchItem(
    val url: String,
    val title: String,
    val preview: String
)

open class SearchResult(
    val signature: SearchEngine,
    val data: List<SearchItem>
) {

    class SearchFail(signature: SearchEngine) : SearchResult(signature, emptyList())

    override fun toString(): String {
        return """
    "$signature": [
        ${data.joinToString(",\n\t\t") { it.toString() }}
    ]
        """
    }

}