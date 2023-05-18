package ru.vsu.cs.tp.richfamily.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vsu.cs.tp.richfamily.api.model.group.Group
import ru.vsu.cs.tp.richfamily.repository.GroupRepository

class GroupViewModel(
    private val groupRepository: GroupRepository,
    private val token: String
) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val groupList = MutableLiveData<List<Group>>()
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

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}