package ru.vsu.cs.tp.richfamily.api.model.operation

data class Operation (
    val id: Int,
    val account: Int,
    val category: Int,
    val op_variant: String,
    val op_date: String,
    val op_recipient: String,
    val op_sum: Float,
    val op_comment: String
)