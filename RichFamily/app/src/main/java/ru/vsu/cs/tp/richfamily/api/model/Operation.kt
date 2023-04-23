package ru.vsu.cs.tp.richfamily.api.model

import java.time.LocalDate
import java.time.LocalTime

data class Operation (
    val id: Int,
    val opName: String,
    val opType: String,
    val opSum: Float,
    val opDate: LocalDate,
    val opTime: LocalTime
)