package server

import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import io.netty.buffer.ByteBuf
import rx.Observable

object ReactiveHttpServer {

    fun process(request: HttpServerRequest<ByteBuf>, response: HttpServerResponse<ByteBuf>): Observable<Void> {
        val command = request.decodedPath
        if (command.isNullOrEmpty()) {
            response.status = HttpResponseStatus.BAD_REQUEST
            return response.writeString(Observable.just("Invalid empty command"))
        }

        val q = request.queryParameters
        val (status, message) = when (command.drop(1)) {
            "all-users" -> Controller.getAllUsers(q)
            "all-items" -> Controller.getAllUsers(q)
            "add-user" -> Controller.addUser(q)
            "add-item" -> Controller.addItem(q)
            "get-user" -> Controller.getUserById(q)
            "get-item" -> Controller.getItemById(q)
            "get-user-items" -> Controller.getAllItemsForUser(q)
            else -> HttpResponseStatus.BAD_REQUEST to Observable.just("Wrong endpoint")
        }

        response.status = status
        return response.writeString(message)
    }

}