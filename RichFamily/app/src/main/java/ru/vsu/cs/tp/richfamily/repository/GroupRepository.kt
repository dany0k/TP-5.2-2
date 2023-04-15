package ru.vsu.cs.tp.richfamily.repository

import androidx.lifecycle.LiveData
import ru.vsu.cs.tp.richfamily.data.GroupDao
import ru.vsu.cs.tp.richfamily.model.Group

class GroupRepository (private val groupDao: GroupDao){
    val allGroups: LiveData<List<Group>> = groupDao.getAllGroups()

    suspend fun insert(groupDao: GroupDao) {
        groupDao.insert(group)
    }

    suspend fun delete(groupDao: GroupDao) {
        groupDao.delete(group)
    }

    suspend fun update(groupDao: GroupDao) {
        groupDao.update(group)
    }

}