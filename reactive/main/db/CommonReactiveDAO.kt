package db

import com.mongodb.client.model.Filters
import com.mongodb.rx.client.MongoDatabase
import com.mongodb.rx.client.Success
import db.entity.Entity
import rx.Observable

open class CommonReactiveDAO<E : Entity>(
    val db: MongoDatabase,
    val collection: String,
    private val factory: Entity.Factory<E>
) {

    fun getAll(): Observable<E> = db
        .getCollection(collection)
        .find()
        .toObservable()
        .map(factory::fromDocument)

    fun getById(id: Int): Observable<E> = db
        .getCollection(collection)
        .find(Filters.eq("id", id))
        .toObservable()
        .first()
        .map(factory::fromDocument)

    fun add(entity: E): Observable<Boolean> = getById(entity.id)
        .singleOrDefault(null)
        .flatMap { found ->
            when (found) {
                null -> {
                    db
                        .getCollection(collection)
                        .insertOne(entity.toDocument())
                        .asObservable()
                        .isEmpty
                        .map { !it }
                }
                else -> Observable.just(false)
            }
        }

    fun deleteAll(): Observable<Success> = db
        .getCollection(collection)
        .drop()

}