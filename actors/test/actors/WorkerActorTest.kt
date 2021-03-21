package actors

import actors.util.InstantClient
import akka.actor.ActorSystem
import akka.actor.Props
import akka.testkit.javadsl.TestKit
import clients.SearchRequest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import search.SearchEngine
import search.SearchResult
import java.time.Duration

class WorkerActorTest {

    private var system: ActorSystem? = null

    @BeforeEach
    fun setUp() {
        system = ActorSystem.create("Test")
    }

    @AfterEach
    fun tearDown() {
        system!!.terminate()
        system = null
    }

    @Test
    fun allOk() {
        object : TestKit(system) {
            init {
                val actor = system!!.actorOf(
                    Props.create(WorkerActor::class.java, InstantClient(SearchEngine.Google))
                )
                within(Duration.ofSeconds(1)) {
                    actor.tell(SearchRequest("test query", 3), testActor)
                    val msg = expectMsgAnyClassOf<SearchResult>(SearchResult::class.java)
                    assertEquals(3, msg.data.size)
                }
            }
        }
    }

}