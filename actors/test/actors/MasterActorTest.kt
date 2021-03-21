package actors

import actors.util.IdleClient
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

class MasterActorTest {

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
                    Props.create(
                        MasterActor::class.java, listOf(
                            InstantClient(SearchEngine.Google),
                            InstantClient(SearchEngine.Yandex)
                        )
                    )
                )
                within(Duration.ofSeconds(2)) {
                    actor.tell(SearchRequest("test query", 3), testActor)
                    val msg = expectMsgAnyClassOf<Map<String, SearchResult>>(Map::class.java)
                    assertEquals(msg.size, 2)
                }
            }
        }
    }

    @Test
    fun partialOk() {
        object : TestKit(system) {
            init {
                val actor = system!!.actorOf(
                    Props.create(
                        MasterActor::class.java, listOf(
                            InstantClient(SearchEngine.Google),
                            IdleClient()
                        )
                    )
                )
                actor.tell(SearchRequest("test query", 3), testActor)
                expectNoMessage(MasterActor.TIMEOUT - Duration.ofSeconds(1))
                within(Duration.ofSeconds(2)) {
                    val msg = expectMsgAnyClassOf<Map<String, SearchResult>>(Map::class.java)
                    assertEquals(msg.size, 1)
                }
            }
        }
    }

}