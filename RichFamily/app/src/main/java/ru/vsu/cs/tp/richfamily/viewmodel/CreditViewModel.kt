package ru.vsu.cs.tp.richfamily.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vsu.cs.tp.richfamily.api.model.credit.Credit
import ru.vsu.cs.tp.richfamily.repository.CreditRepository

class CreditViewModel(
    private val creditRepository: CreditRepository,
    private val token: String
) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val creditList = MutableLiveData<List<Credit>>()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    fun getAllCredit() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = creditRepository.getAllCredits()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    creditList.postValue(response.body())
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