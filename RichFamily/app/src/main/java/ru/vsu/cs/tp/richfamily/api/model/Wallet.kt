package ru.vsu.cs.tp.richfamily.api.model

data class Wallet(
    val id: Int,
    val user: Int,
    val acc_sum: Int,
    val acc_currency: String,
    val acc_comment: String
)
