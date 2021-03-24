package se.doreshnikov.dao

import se.doreshnikov.model.Shares

interface SharesDAO {

    fun getShares(): List<Shares>

    fun getShares(companyName: String): Shares

    fun addShares(companyName: String, price: Long)

    fun increaseShares(companyName: String, amount: Long): Long

    fun decreaseShares(companyName: String, amount: Long): Long

    fun changePrice(companyName: String, newPrice: Long)

}