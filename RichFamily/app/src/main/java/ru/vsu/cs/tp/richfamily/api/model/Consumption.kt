package ru.vsu.cs.tp.richfamily.api.model

import java.time.LocalDate
import java.time.LocalTime

data class Consumption (
    val id: Int,
    val consName: String,
    val consSum: Float,
    val consDate: LocalDate,
    val consTime: LocalTime
    )
