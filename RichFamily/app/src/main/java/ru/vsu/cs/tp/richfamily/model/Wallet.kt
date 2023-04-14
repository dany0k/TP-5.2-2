package ru.vsu.cs.tp.richfamily.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "wallet")
data class Wallet(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "title")
    val walletTitle: String,
    @ColumnInfo(name = "score")
    val walletScore: Int,
    @ColumnInfo(name = "comment")
    val walletComment: String
) : Parcelable