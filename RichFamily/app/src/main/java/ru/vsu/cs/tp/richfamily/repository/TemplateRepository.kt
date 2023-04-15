package ru.vsu.cs.tp.richfamily.repository

import androidx.lifecycle.LiveData
import ru.vsu.cs.tp.richfamily.data.TemplateDao
import ru.vsu.cs.tp.richfamily.model.Template

class TemplateRepository (private val templateDao: TemplateDao){
    val allTemplates: LiveData<List<Template>> = templateDao.getAllTemplates()

    suspend fun insert(template: Template) {
        templateDao.insert(template)
    }

    suspend fun delete(template: Template) {
        templateDao.delete(template)
    }

    suspend fun update(template: Template) {
        templateDao.update(template)
    }

}