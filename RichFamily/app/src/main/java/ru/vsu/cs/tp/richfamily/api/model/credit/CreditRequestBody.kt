package ru.vsu.cs.tp.richfamily.api.model.credit

data class CreditRequestBody (
    val user: Int,
    val cr_name: String,
    val cr_all_sum: Float,
    val cr_percent: Int,
    val cr_period: Int,
    val cr_month_pay: Float,
    val cr_percents_sum: Float,
    val cr_sum_plus_percents: Float
)