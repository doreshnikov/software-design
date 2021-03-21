package db.entity

import org.bson.Document
import util.Currency

data class Item(
    override val id: Int,
    val name: String,
    val price: Double,
    val currency: Currency
) : Entity {

    companion object : Entity.Factory<Item> {
        override fun fromDocument(document: Document): Item = Item(
            document.getInteger("id"),
            document.getString("name"),
            document.getDouble("price"),
            Currency.valueOf(document.getString("currency"))
        )
    }

    override fun toDocument(): Document = Document(mapOf(
        "id" to id,
        "name" to name,
        "price" to price,
        "currency" to currency.name
    ))

}
