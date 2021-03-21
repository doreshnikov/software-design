
import io.reactivex.netty.protocol.http.server.HttpServer
import org.apache.log4j.BasicConfigurator
import server.ReactiveHttpServer

fun main() {

    val processor = ReactiveHttpServer
    BasicConfigurator.configure()

    HttpServer
        .newServer(8081)
        .start { req, resp ->
            processor.process(req, resp)
        }
        .awaitShutdown()

}