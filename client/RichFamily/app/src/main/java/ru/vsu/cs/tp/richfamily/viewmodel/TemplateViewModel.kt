package ru.vsu.cs.tp.richfamily.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vsu.cs.tp.richfamily.api.model.template.Template
import ru.vsu.cs.tp.richfamily.api.model.template.TemplateRequestBody
import ru.vsu.cs.tp.richfamily.repository.TemplateRepository

class TemplateViewModel(
    private val templateRepository: TemplateRepository,
    private val token: String
) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val templatesList = MutableLiveData<List<Template>>()
    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    fun getAllTemplates() {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = templateRepository.getAllTemplates()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    templatesList.postValue(response.body()!!)
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    fun isScoreValid(score: String): Boolean {
        val regex = Regex("(-?\\d*\\.?\\d+)")
        val matchResult = regex.find(score)
        return matchResult != null
    }

    fun addTemplate(
        tempName: String,
        walletId: Int,
        categoryId: Int,
        opType: String,
        opRecipient: String,
        opSum: Float,
        opComment: String
    ) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = templateRepository.addTemplate(
                TemplateRequestBody(
                    account = walletId,
                    category = categoryId,
                    temp_name = tempName,
                    temp_variant = opType,
                    temp_recipient = opRecipient,
                    temp_sum = opSum,
                    temp_comment = opComment
                )
            )
            withContext(Dispatchers.Main) {
                if (!response.isSuccessful) {
                    onError("Error : ${response.message()} ")
                } else {
                    getAllTemplates()
                }
            }
        }
    }

    fun deleteTemplate(id: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = templateRepository.deleteTemplate(token = token, id = id)
            withContext(Dispatchers.Main) {
                if (!response.isSuccessful) {
                    onError("Error : ${response.message()} ")
                } else {
                    getAllTemplates()
                }
            }
        }
    }

    fun editOperation(
        id: Int,
        name: String,
        walletId: Int,
        categoryId: Int,
        opType: String,
        opRecipient: String,
        opSum: Float,
        opComment: String
    ) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = templateRepository.editTemplate(
                templateRequestBody = TemplateRequestBody(
                    temp_name = name,
                    account = walletId,
                    category = categoryId,
                    temp_variant = opType,
                    temp_recipient = opRecipient,
                    temp_sum = opSum,
                    temp_comment = opComment
                ),
                id = id
            )
            withContext(Dispatchers.Main) {
                if (!response.isSuccessful) {
                    onError("Error : ${response.message()} ")
                } else {
                    getAllTemplates()
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