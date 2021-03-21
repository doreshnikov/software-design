import com.beust.klaxon.Klaxon
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.path
import org.http4k.server.ApacheServer
import org.http4k.server.asServer

class Run {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Run()::processRequest.asServer(ApacheServer(8081)).start()
        }
    }

    val users = listOf(
        mapOf(
            "id" to 1,
            "name" to "User1",
            "currency" to "USD"
        ),
        mapOf(
            "id" to 2,
            "name" to "User2",
            "currency" to "EUR"
        ),
        mapOf(
            "id" to 3,
            "name" to "User3",
            "currency" to "RUB"
        )
    )

    val items = listOf(
        mapOf(
            "id" to 1,
            "name" to "Cheap item",
            "price" to 1.12,
            "currency" to "EUR"
        ),
        mapOf(
            "id" to 2,
            "name" to "Common item",
            "price" to 10.23,
            "currency" to "USD"
        ),
        mapOf(
            "id" to 3,
            "name" to "Expensive item",
            "price" to 20000.00,
            "currency" to "RUB"
        )
    )

    val currencies = mapOf(
        "EUR" to 85.00,
        "USD" to 73.12,
        "RUB" to 1.00
    )

    private fun processRequest(request: Request): Response {
        val cmd = request.uri.path
        return when (cmd) {
            "/get-items" -> Response(Status.OK).body(Klaxon().toJsonString(items))
            "/get-items-for" -> Response(Status.OK).body(Klaxon().toJsonString(items.map {
                val res = mutableMapOf<String, Any>()
                it.forEach { (t, u) -> res[t] = u }
                val user = users.first { user -> user["id"] == request.query("id")!!.toInt() }
                res["price"] =
                    (res["price"] as Double) * currencies[res["currency"] as String]!! / currencies[user["currency"] as String]!!
                res["currency"] = user["currency"]!!
                res
            }))
            "/get-users" -> Response(Status.OK).body(Klaxon().toJsonString(users))
            else -> throw Exception("")
        }
    }

}