package ru.vsu.cs.tp.richfamily.repository

import androidx.lifecycle.LiveData
import ru.vsu.cs.tp.richfamily.data.CreditDao
import ru.vsu.cs.tp.richfamily.model.Credit

class CreditRepository (private val creditDao: CreditDao){
    val allCredits: LiveData<List<Credit>> = creditDao.getAllCredits()

    suspend fun insert(credit: Credit) {
        creditDao.insert(credit)
    }

    suspend fun delete(credit: Credit) {
        creditDao.delete(credit)
    }

    suspend fun update(credit: Credit) {
        creditDao.update(credit)
    }

}