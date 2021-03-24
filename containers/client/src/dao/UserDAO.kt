package se.doreshnikov.dao

import se.doreshnikov.model.User

interface UserDAO {

    fun getUser(id: Long): User

    fun addUser(id: Long, name: String)

    fun increaseBalance(id: Long, amount: Long)

    fun decreaseBalance(id: Long, amount: Long)

    fun addShares(id: Long, companyName: String, amount: Long)

    fun removeShares(id: Long, companyName: String, amount: Long)

}