package ru.vsu.cs.tp.richfamily.repository

import androidx.lifecycle.LiveData
import ru.vsu.cs.tp.richfamily.model.Operations
import ru.vsu.cs.tp.richfamily.data.OperationsDao

class OperationsRepository (private val operationsDao: OperationsDao){
    val allTemplates: LiveData<List<Operations>> = operationsDao.getAllOperations()

    suspend fun insert(operations: Operations) {
        operationsDao.insert(operations)
    }

    suspend fun delete(operations: Operations) {
        operationsDao.delete(operations)
    }

    suspend fun update(operations: Operations) {
        operationsDao.update(operations)
    }

}