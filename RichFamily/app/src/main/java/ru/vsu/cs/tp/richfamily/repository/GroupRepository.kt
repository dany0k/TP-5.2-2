package ru.vsu.cs.tp.richfamily.repository

import androidx.lifecycle.LiveData
import ru.vsu.cs.tp.richfamily.data.GroupDao
import ru.vsu.cs.tp.richfamily.model.Group

class GroupRepository (private val groupDao: GroupDao){
    val allGroups: LiveData<List<Group>> = groupDao.getAllGroups()

    suspend fun insert(group: Group) {
        groupDao.insert(group)
    }

    suspend fun delete(group: Group) {
        groupDao.delete(group)
    }

    suspend fun update(group: Group) {
        groupDao.update(group)
    }

}