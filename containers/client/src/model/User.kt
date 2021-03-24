package se.doreshnikov.model

data class User(
    val id: Long,
    val name: String,
    val balance: Long,
    val shares: List<Shares>
)