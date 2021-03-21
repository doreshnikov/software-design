package actors

import akka.actor.AbstractActor
import clients.SearchClient
import clients.SearchRequest

class WorkerActor(
    val client: SearchClient
) : AbstractActor() {

    override fun createReceive(): Receive = receiveBuilder()
        .match(SearchRequest::class.java, this::processRequest)
        .build()

    private fun processRequest(request: SearchRequest) {
        val result = client.requestTop(request)
        sender.tell(result, self)
    }

}