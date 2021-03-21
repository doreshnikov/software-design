package db.entity

import org.bson.Document

interface Entity {

    val id: Int

    interface Factory<E : Entity> {
        fun fromDocument(document: Document): E
    }

    fun toDocument(): Document

}