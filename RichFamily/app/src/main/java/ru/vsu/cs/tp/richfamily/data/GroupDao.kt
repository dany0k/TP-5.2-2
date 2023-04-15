package ru.vsu.cs.tp.richfamily.data

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.vsu.cs.tp.richfamily.model.Group

interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(group: Group)

    @Delete
    suspend fun delete(group: Group)

    @Update
    suspend fun update(group: Group)

    @Query("Select * from userGroup order by id ASC")
    fun getAllGroups(): LiveData<List<Group>>
}