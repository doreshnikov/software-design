package search

enum class SearchEngine(
    val baseURL: String
) {
    Google("https://google.com/"),
    Bing("https://bing.com/"),
    DuckDuckGo("https://duckduckgo.com/"),
    Yandex("https://yandex.ru/")
}