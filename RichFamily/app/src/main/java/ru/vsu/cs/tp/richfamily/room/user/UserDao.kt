package ru.vsu.cs.tp.richfamily.room.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insert(userDB: UserDB)

    @Query("SELECT * FROM user LIMIT 1")
    fun getUser(): UserDB

    @Query("SELECT COUNT(*) FROM user")
    fun countUser(): Int
}