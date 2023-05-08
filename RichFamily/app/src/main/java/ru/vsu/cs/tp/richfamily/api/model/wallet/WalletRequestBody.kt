package ru.vsu.cs.tp.richfamily.api.model.wallet

data class WalletRequestBody(
    val user: Int,
    val acc_name: String,
    val acc_sum: Float,
    val acc_currency: String,
    val acc_comment: String
)
