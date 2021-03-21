package db.entity

import org.bson.Document
import util.Currency

data class User(
    override val id: Int,
    val name: String,
    val currency: Currency
) : Entity {

    companion object : Entity.Factory<User> {
        override fun fromDocument(document: Document): User = User(
            document.getInteger("id"),
            document.getString("name"),
            Currency.valueOf(document.getString("currency"))
        )
    }

    override fun toDocument(): Document = Document(mapOf(
        "id" to id,
        "name" to name,
        "currency" to currency.name
    ))

}
