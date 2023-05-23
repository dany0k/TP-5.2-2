package ru.vsu.cs.tp.richfamily.api.model.operation

import java.time.LocalDate
import java.time.LocalTime

data class Income (
    val id: Int,
    val incomeName: String,
    val incomeSum: Float,
    val incomeDate: LocalDate,
    val incomeTime: LocalTime
)