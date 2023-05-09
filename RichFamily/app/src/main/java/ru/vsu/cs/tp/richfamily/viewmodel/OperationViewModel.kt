package ru.vsu.cs.tp.richfamily.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.vsu.cs.tp.richfamily.api.model.operation.Operation
import ru.vsu.cs.tp.richfamily.api.model.operation.OperationRequestBody
import ru.vsu.cs.tp.richfamily.repository.OperationRepository

class OperationViewModel(
    private val operationRepository: OperationRepository,
    private val token: String
) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val inList = MutableLiveData<List<Operation>>()
    val consList = MutableLiveData<List<Operation>>()
    val currentOperation = MutableLiveData<Operation>()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    fun getOperationById(id: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = operationRepository.getOperationById(id = id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    currentOperation.value = response.body()
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }
    fun getAllOperations() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = operationRepository.getAllOperations()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    consList.postValue(response.body()!!.filter { it.op_variant == "РАСХОД" })
                    inList.postValue(response.body()!!.filter { it.op_variant == "ДОХОД" })
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    fun addOperation(
        walletId: Int,
        categoryId: Int,
        opType: String,
        opDate: String,
        opRecipient: String,
        opSum: Float,
        opComment: String
    ) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = operationRepository.addOperation(
                OperationRequestBody(
                    account = walletId,
                    category = categoryId,
                    op_variant = opType,
                    op_date = opDate,
                    op_recipient = opRecipient,
                    op_sum = opSum,
                    op_comment = opComment
                )
            )
            withContext(Dispatchers.Main) {
                if (!response.isSuccessful) {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    fun deleteOperation(id: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = operationRepository.deleteOperation(token = token, id = id)
            withContext(Dispatchers.Main) {
                if (!response.isSuccessful) {
                    onError("Error : ${response.message()} ")
                } else {
                    getAllOperations()
                }
            }
        }
    }

    fun editOperation(
        id: Int,
        walletId: Int,
        categoryId: Int,
        opType: String,
        opDate: String,
        opRecipient: String,
        opSum: Float,
        opComment: String
    ) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = operationRepository.editOperation(
                operationRequestBody = OperationRequestBody(
                    account = walletId,
                    category = categoryId,
                    op_variant = opType,
                    op_date = opDate,
                    op_recipient = opRecipient,
                    op_sum = opSum,
                    op_comment = opComment
                ),
                id = id
            )
            withContext(Dispatchers.Main) {
                if (!response.isSuccessful) {
                    onError("Error : ${response.message()} ")
                } else {
                    getAllOperations()
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