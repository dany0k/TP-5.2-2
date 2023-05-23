package ru.vsu.cs.tp.richfamily.api.model.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfile (
    val id: Int,
    val first_name: String,
    val last_name: String,
    val email: String
) : Parcelable