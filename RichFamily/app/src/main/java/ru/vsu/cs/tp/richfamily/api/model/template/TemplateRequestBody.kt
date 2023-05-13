package ru.vsu.cs.tp.richfamily.api.model.template

data class TemplateRequestBody(
    val category: Int,
    val account: Int,
    val temp_name: String,
    val temp_variant: String,
    val temp_recipient: String,
    val temp_sum: Float,
    val temp_comment: String
)