package ru.vsu.cs.tp.richfamily.data

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.vsu.cs.tp.richfamily.model.Operations

interface OperationsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(operations: Operations)

    @Delete
    suspend fun delete(operations: Operations)

    @Update
    suspend fun update(operations: Operations)

    @Query("Select * from operations order by id ASC")
    fun getAllOperations(): LiveData<List<Operations>>
}