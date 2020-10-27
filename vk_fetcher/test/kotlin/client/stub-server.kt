package client

import com.xebialabs.restito.server.StubServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope

suspend fun withStubServer(port: Int, callback: suspend CoroutineScope.(StubServer) -> Unit) {
    var stubServer: StubServer? = null
    try {
        stubServer = StubServer(port).run()
        GlobalScope.callback(stubServer)
    } finally {
        stubServer?.stop()
    }
}