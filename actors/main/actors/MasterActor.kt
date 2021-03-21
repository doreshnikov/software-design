package actors

import akka.actor.AbstractActor
import akka.actor.ActorRef
import akka.actor.Props
import clients.SearchClient
import clients.SearchRequest
import search.SearchResult
import java.time.Duration

class MasterActor(
    val clients: List<SearchClient>
) : AbstractActor() {

    private lateinit var returnPoint: ActorRef
    private val results = mutableMapOf<String, SearchResult>()

    private class TimeoutSignal
    class TerminateSignal

    companion object {
        val TIMEOUT: Duration = Duration.ofSeconds(5)!!
    }

    override fun createReceive(): Receive = receiveBuilder()
        .match(SearchRequest::class.java, this::processRequest)
        .match(SearchResult::class.java, this::processSearchResult)
        .match(TimeoutSignal::class.java) { commitResult() }
        .match(TerminateSignal::class.java) { context.stop(self) }
        .build()

    private fun processRequest(request: SearchRequest) {
        returnPoint = sender
        clients
            .map { context.actorOf(Props.create(WorkerActor::class.java, it)) }
            .forEach { it.tell(request, self) }
        context.system.scheduler.scheduleOnce(
            TIMEOUT, self, TimeoutSignal(),
            context.system.dispatcher, ActorRef.noSender()
        )
    }

    private fun processSearchResult(result: SearchResult) {
        results[result.signature.name] = result
        if (results.size == clients.size) {
            commitResult()
            self.tell(TerminateSignal(), self)
        }
    }

    private fun commitResult() {
        returnPoint.tell(results, self)
    }

}