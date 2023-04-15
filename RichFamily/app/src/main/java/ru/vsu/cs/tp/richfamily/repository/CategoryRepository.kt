package ru.vsu.cs.tp.richfamily.repository

import androidx.lifecycle.LiveData
import ru.vsu.cs.tp.richfamily.data.CategoryDao
import ru.vsu.cs.tp.richfamily.model.Category


class CategoryRepository (private val categoryDao: CategoryDao){
    val allCategories: LiveData<List<Category>> = categoryDao.getAllCategory()

    suspend fun insert(category: Category) {
        categoryDao.insert(category)
    }

    suspend fun delete(category: Category) {
        categoryDao.delete(category)
    }

    suspend fun update(category: Category) {
        categoryDao.update(category)
    }
}