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
import ru.vsu.cs.tp.richfamily.api.model.credit.CreditRequestBody
import ru.vsu.cs.tp.richfamily.repository.CreditRepository

class CreditViewModel(
    private val creditRepository: CreditRepository,
    private val token: String
) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val creditList = MutableLiveData<List<Credit>>()
    private var job: Job? = null
    var curCredit = MutableLiveData<Credit>()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    fun getAllCredits() {
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

    fun addCredit(
        crName: String,
        crAllSum: Float,
        crPerc: Int,
        crPeriod: Int,
        crFirstPay: Float,
        crMonthPay: Float,
        crPercSum: Float,
        crTotalSum: Float) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = creditRepository.addCredit(
                token,
                CreditRequestBody(
                    user = 1,
                    cr_name = crName,
                    cr_all_sum = crAllSum,
                    cr_percent = crPerc,
                    cr_first_pay = crFirstPay,
                    cr_period = crPeriod,
                    cr_month_pay = crMonthPay,
                    cr_percents_sum = crPercSum,
                    cr_sum_plus_percents = crTotalSum
                )
            )
            withContext(Dispatchers.Main) {
                if (!response.isSuccessful) {
                    onError("Error : ${response.message()} ")
                } else {
                    curCredit.value = response.body()
                }
            }
        }
    }

    fun addCreditNotAuth(
        crName: String,
        crAllSum: Float,
        crPerc: Int,
        crPeriod: Int,
        crFirstPay: Float
    ) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = creditRepository.addCreditNotAuth(
                CreditRequestBody(
                    user = -1,
                    cr_name = crName,
                    cr_all_sum = crAllSum,
                    cr_percent = crPerc,
                    cr_period = crPeriod,
                    cr_first_pay = crFirstPay,
                    cr_month_pay = 0F,
                    cr_percents_sum = 0F,
                    cr_sum_plus_percents = 0F
                )
            )
            withContext(Dispatchers.Main) {
                if (!response.isSuccessful) {
                    onError("Error : ${response.message()} ")
                } else {
                    curCredit.value = response.body()
                }
            }
        }
    }

    fun deleteCredit(id: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = creditRepository.deleteCredit(token = token, id = id)
            withContext(Dispatchers.Main) {
                if (!response.isSuccessful) {
                    onError("Error : ${response.message()} ")
                } else {

                    getAllCredits()
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