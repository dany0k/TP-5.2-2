package ru.vsu.cs.tp.richfamily.data

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.vsu.cs.tp.richfamily.model.Credit

interface CreditDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(credit: Credit)

    @Delete
    suspend fun delete(credit: Credit)

    @Update
    suspend fun update(credit: Credit)

    @Query("Select * from credit order by id ASC")
    fun getAllCredits(): LiveData<List<Credit>>
}