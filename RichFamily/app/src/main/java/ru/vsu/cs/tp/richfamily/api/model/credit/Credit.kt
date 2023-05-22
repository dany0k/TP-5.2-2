package ru.vsu.cs.tp.richfamily.api.model.credit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Credit(
    var id: Int,
    val user: Int,
    val cr_name: String,
    val cr_first_pay: String,
    val cr_all_sum: Float,
    val cr_percent: Int,
    val cr_period: Int,
    val cr_month_pay: Float,
    val cr_percents_sum: Float,
    val cr_sum_plus_percents: Float
) : Parcelable