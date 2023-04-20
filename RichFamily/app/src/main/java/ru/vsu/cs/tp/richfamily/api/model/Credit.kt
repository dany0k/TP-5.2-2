package ru.vsu.cs.tp.richfamily.api.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "credit")

data class Credit (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "creditName")
    val creditName: String,
    @ColumnInfo(name = "percent")
    val percent: Int,
    @ColumnInfo(name = "firstPay")
    val firstPay: Float,
    @ColumnInfo(name = "creditSum")
    val creditSum: Float,
    @ColumnInfo(name = "period")
    val period: Int
        ) : Parcelable