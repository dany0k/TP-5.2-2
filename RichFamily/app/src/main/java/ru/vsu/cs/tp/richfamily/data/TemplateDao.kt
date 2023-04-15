package ru.vsu.cs.tp.richfamily.data

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.vsu.cs.tp.richfamily.model.Template

interface TemplateDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(template: Template)

    @Delete
    suspend fun delete(template: Template)

    @Update
    suspend fun update(template: Template)

    @Query("Select * from template order by id ASC")
    fun getAllTemplates(): LiveData<List<Template>>
}