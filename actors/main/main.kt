
import actors.MasterActor
import akka.actor.AbstractActor
import akka.actor.ActorSystem
import akka.actor.Props
import clients.SearchRequest
import clients.StubSearchClientFactory
import search.SearchEngine
import java.util.concurrent.atomic.AtomicBoolean

val factory = StubSearchClientFactory()
val allClients = SearchEngine.values().map { factory.getClient(it) }

var waiting = AtomicBoolean(false)

class ListenerActor : AbstractActor() {
    override fun createReceive(): Receive = receiveBuilder()
        .match(Any::class.java) {
            (it as Map<*, *>).values.forEach(::print)
            waiting.set(false)
        }
        .build()
}

fun main() {

    val system = ActorSystem.create()
    val listener = system.actorOf(Props.create(ListenerActor::class.java))

    while (true) {
        while (waiting.get()) {
            Thread.sleep(100)
        }
        println("Please enter a query to send:")
        val query = readLine()
        println("Please enter a number of results to show (default is 5):")
        var count = 5
        try {
            count = readLine()!!.toInt()
        } catch (e: Exception) {}

        waiting.set(true)

        if (query.isNullOrEmpty()) {
            system.terminate()
            return
        }

        val master = system.actorOf(Props.create(MasterActor::class.java, allClients))
        master.tell(SearchRequest(query, count), listener)
    }

}