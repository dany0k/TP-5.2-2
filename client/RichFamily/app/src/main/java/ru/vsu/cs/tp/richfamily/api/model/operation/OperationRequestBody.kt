package ru.vsu.cs.tp.richfamily.api.model.operation

data class OperationRequestBody(
    val account: Int,
    val category: Int,
    val op_variant: String,
    val op_date: String,
    val op_recipient: String,
    val op_sum: Float,
    val op_comment: String
)