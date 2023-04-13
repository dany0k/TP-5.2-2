package ru.vsu.cs.tp.richfamily.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallet")
class Wallet(
    @ColumnInfo(name = "title")
    val walletTitle: String,
    @ColumnInfo(name = "score")
    val walletScore: Int,
    @ColumnInfo(name = "comment")
    val walletComment: String
)  {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}