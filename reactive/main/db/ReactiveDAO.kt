package db

import com.mongodb.rx.client.MongoClients
import db.entity.Item
import db.entity.User
import rx.Observable
import util.Currency

object ReactiveDAO {

    private val database = MongoClients
        .create("mongodb://localhost:27017")
        .getDatabase("reactive")

    object Users : CommonReactiveDAO<User>(database, "user", User.Companion)
    object Items : CommonReactiveDAO<Item>(database, "item", Item.Companion)

    fun getItemsForUser(userId: Int): Observable<Item> = Users
        .getById(userId)
        .flatMap { user ->
            Items
                .getAll()
                .map {
                    val newPrice = Currency.convert(it.price, it.currency, user.currency)
                    Item(it.id, it.name, newPrice, user.currency)
                }
        }

}