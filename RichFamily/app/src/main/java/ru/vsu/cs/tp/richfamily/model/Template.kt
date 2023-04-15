package ru.vsu.cs.tp.richfamily.model

import android.os.Parcelable
import androidx.compose.ui.text.input.TextFieldValue
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Parcelize
@Entity(tableName = "template")

data class Template(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "wallet")
    val wallet: Wallet,
    @ColumnInfo(name = "date")
    val date: LocalDateTime,
    @ColumnInfo(name = "recipient")
    val recipient: String,
    @ColumnInfo(name = "category")
    val category: Category,
    @ColumnInfo(name = "sum")
    val sum: Float,
    @ColumnInfo(name = "comment")
    val comment: String,
) : Parcelable