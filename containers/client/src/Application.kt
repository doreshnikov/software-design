package se.doreshnikov

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import se.doreshnikov.dao.SharesDAO
import se.doreshnikov.dao.UserDAO
import se.doreshnikov.dao.impl.HttpSharesDAO
import se.doreshnikov.dao.impl.InMemoryUserDAO

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    val userDAO: UserDAO = InMemoryUserDAO
    val sharesDAO: SharesDAO = HttpSharesDAO(
        "http://localhost:49152",
        userDAO
    )

    routing {
        get("/register") {
            try {
                val id = call.requireParameter<Long>("user-id")
                val name = call.requireParameter<String>("name")
                userDAO.addUser(id, name)
                call.respond("SUCCESS")
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        get("/profile") {
            try {
                val id = call.requireParameter<Long>("user-id")
                call.respond(userDAO.getUser(id))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        get("/increase") {
            try {
                val id = call.requireParameter<Long>("user-id")
                val amount = call.requireParameter<Long>("amount")
                userDAO.increaseBalance(id, amount)
                call.respond("SUCCESS")
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        get("/totals") {
            try {
                val id = call.requireParameter<Long>("user-id")
                val newShares = sharesDAO.getTotalWorth(
                    userDAO.getUser(id).shares
                )
                call.respond(
                    mapOf(
                        "details" to newShares,
                        "total" to newShares.map { it.amount * it.price!! }.sum()
                    )
                )
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        get("/buy") {
            try {
                val id = call.requireParameter<Long>("user-id")
                val companyName = call.requireParameter<String>("company-name")
                val amount = call.requireParameter<Long>("amount")

                sharesDAO.buy(id, companyName, amount)
                call.respond("SUCCESS")
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }

        get("/sell") {
            try {
                val id = call.requireParameter<Long>("user-id")
                val companyName = call.requireParameter<String>("company-name")
                val amount = call.requireParameter<Long>("amount")

                sharesDAO.sell(id, companyName, amount)
                call.respond("SUCCESS")
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }
    }
}

