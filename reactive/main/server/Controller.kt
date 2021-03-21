package server

import db.ReactiveDAO
import db.entity.Item
import db.entity.User
import io.netty.handler.codec.http.HttpResponseStatus
import rx.Observable
import util.Currency

object Controller {

    fun checkAndExecute(
        q: Map<String, List<String>>,
        required: Map<String, Class<*>>,
        action: (Map<String, List<String>>) -> Observable<String>
    ): Pair<HttpResponseStatus, Observable<String>> {
        for ((k, v) in required) {
            if (k !in q.keys) {
                return HttpResponseStatus.BAD_REQUEST to Observable.just("Missing parameter '$k'")
            }
            when (v) {
                Int::class.java -> {
                    try {
                        q[k]!![0].toInt()
                    } catch (e: Exception) {
                        return HttpResponseStatus.BAD_REQUEST to Observable.just("Can't parse '$k=${q[k]}' to Int")
                    }
                }
                Double::class.java -> {
                    try {
                        q[k]!![0].toDouble()
                    } catch (e: Exception) {
                        return HttpResponseStatus.BAD_REQUEST to Observable.just("Can't parse '$k=${q[k]}' to Double")
                    }
                }
                Currency::class.java -> {
                    if (q[k]!![0].toUpperCase() !in Currency.values().map { it.name }) {
                        return HttpResponseStatus.BAD_REQUEST to Observable.just("Invalid '$k=${q[k]}' currency name")
                    }
                }
                else -> {
                }
            }
        }
        return HttpResponseStatus.OK to action(q)
    }

    fun addUser(q: Map<String, List<String>>): Pair<HttpResponseStatus, Observable<String>> {
        return checkAndExecute(
            q, mapOf(
                "id" to Int::class.java,
                "name" to String::class.java,
                "currency" to Currency::class.java
            )
        ) {
            return@checkAndExecute ReactiveDAO.Users.add(
                User(
                    q["id"]!![0].toInt(),
                    q["name"]!![0],
                    Currency.valueOf(q["currency"]!![0].toUpperCase())
                )
            ).map { it.toString() }
        }
    }

    fun addItem(q: Map<String, List<String>>): Pair<HttpResponseStatus, Observable<String>> {
        return checkAndExecute(
            q, mapOf(
                "id" to Int::class.java,
                "name" to String::class.java,
                "price" to Double::class.java,
                "currency" to Currency::class.java
            )
        ) {
            return@checkAndExecute ReactiveDAO.Items.add(
                Item(
                    q["id"]!![0].toInt(),
                    q["name"]!![0],
                    q["price"]!![0].toDouble(),
                    Currency.valueOf(q["currency"]!![0].toUpperCase())
                )
            ).map { it.toString() }
        }
    }

    fun getUserById(q: Map<String, List<String>>): Pair<HttpResponseStatus, Observable<String>> {
        return checkAndExecute(q, mapOf("id" to Int::class.java)) {
            return@checkAndExecute ReactiveDAO.Users.getById(q["id"]!![0].toInt()).map { it.toString() }
        }
    }

    fun getItemById(q: Map<String, List<String>>): Pair<HttpResponseStatus, Observable<String>> {
        return checkAndExecute(q, mapOf("id" to Int::class.java)) {
            return@checkAndExecute ReactiveDAO.Items.getById(q["id"]!![0].toInt()).map { it.toString() }
        }
    }

    fun getAllUsers(q: Map<String, List<String>>): Pair<HttpResponseStatus, Observable<String>> {
        return checkAndExecute(q, emptyMap()) {
            return@checkAndExecute ReactiveDAO.Users.getAll().map { it.toString() }
        }
    }

    fun getAllItems(q: Map<String, List<String>>): Pair<HttpResponseStatus, Observable<String>> {
        return checkAndExecute(q, emptyMap()) {
            return@checkAndExecute ReactiveDAO.Users.getAll().map { it.toString() }
        }
    }

    fun getAllItemsForUser(q: Map<String, List<String>>): Pair<HttpResponseStatus, Observable<String>> {
        return checkAndExecute(q, mapOf("id" to Int::class.java)) {
            return@checkAndExecute ReactiveDAO.getItemsForUser(q["id"]!![0].toInt()).map { it.toString() }
        }
    }

}