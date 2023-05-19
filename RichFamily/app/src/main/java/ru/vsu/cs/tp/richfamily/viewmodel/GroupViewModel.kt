package ru.vsu.cs.tp.richfamily.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vsu.cs.tp.richfamily.api.model.group.DeleteUserRequestBody
import ru.vsu.cs.tp.richfamily.api.model.group.Group
import ru.vsu.cs.tp.richfamily.api.model.group.GroupRequestBody
import ru.vsu.cs.tp.richfamily.api.model.group.GroupUser
import ru.vsu.cs.tp.richfamily.api.model.group.GroupUserRequestBody
import ru.vsu.cs.tp.richfamily.repository.GroupRepository

class GroupViewModel(
    private val groupRepository: GroupRepository,
    private val token: String
) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val groupList = MutableLiveData<List<Group>>()
    val usersList = MutableLiveData<List<GroupUser>>()
    val isLeader = MutableLiveData<Boolean>()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    fun getUsersGroup() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = groupRepository.getUsersGroup()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    groupList.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    fun getAllUsersFromGroup(id: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = groupRepository.getAllUsersFromGroup(id = id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    usersList.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    fun addUserInGroup(id: Int, email: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = groupRepository.addUserInGroup(
                groupId = id,
                groupUserRequestBody = GroupUserRequestBody(
                    username = email)
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loading.value = false
                    getAllUsersFromGroup(id = id)
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    fun deleteUserFromGroup(groupId: Int, userId: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = groupRepository.deleteUserFromGroup(
                groupId = groupId,
                deleteUserRequestBody = DeleteUserRequestBody(
                    user_id = userId)
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loading.value = false
                    getAllUsersFromGroup(id = groupId)
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    fun addGroup(grName: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = groupRepository.addGroup(
                groupRequestBody = GroupRequestBody(
                    gr_name = grName
                )
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    fun isLeader(groupId: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = groupRepository.isLeader(
                    groupId = groupId
                )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    isLeader.postValue(response.body()!!.is_leader)
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    fun leaveFromGroup(groupId: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = groupRepository.leaveFromGroup(
                groupId = groupId
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    getUsersGroup()
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}