package ru.vsu.cs.tp.richfamily.api.model.credit

data class CreditRequestBody (
    var id: Int = 0,
    val creditName: String,
    val percent: Int,
    val firstPay: Float,
    val creditSum: Float,
    val period: Int
)