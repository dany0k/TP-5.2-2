package ru.vsu.cs.tp.richfamily.api.model.wallet

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wallet(
    val id: Int,
    val user: Int,
    val acc_name: String,
    val acc_sum: Float,
    val acc_currency: String,
    val acc_comment: String
) : Parcelable
