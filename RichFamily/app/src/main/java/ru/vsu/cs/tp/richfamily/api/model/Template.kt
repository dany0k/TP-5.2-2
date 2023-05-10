package ru.vsu.cs.tp.richfamily.model

import ru.vsu.cs.tp.richfamily.api.model.Category

data class Template(
    val id: Int,
    val category: Int,
    val temp_name: String,
    val temp_variant: String,
    val temp_recipient: String,
    val temp_sum: Float
)