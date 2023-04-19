package ru.vsu.cs.tp.richfamily.room.user

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class UserDB (
    @PrimaryKey val id: Int,
    val email: String,
    val token: String
) : Parcelable

