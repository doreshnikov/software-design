package se.doreshnikov.dao

import se.doreshnikov.model.Shares

interface SharesDAO {

    suspend fun buy(userId: Long, companyName: String, amount: Long)

    suspend fun sell(userId: Long, companyName: String, amount: Long)

    suspend fun getTotalWorth(shares: List<Shares>): List<Shares>

}