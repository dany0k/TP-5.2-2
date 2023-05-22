package ru.vsu.cs.tp.richfamily.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.vsu.cs.tp.richfamily.api.model.wallet.Wallet
import ru.vsu.cs.tp.richfamily.api.model.wallet.WalletRequestBody
import ru.vsu.cs.tp.richfamily.repository.WalletRepository

class WalletViewModel(
    private val walletRepository: WalletRepository,
    private val token: String
) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val walletList = MutableLiveData<List<Wallet>>()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    fun getAllWallets() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = walletRepository.getAllWallets()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    walletList.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    fun getWalletFromACTV(selectedItem: String, walList: List<Wallet>): Int {
        val selectedClass = walList.find {
            "${it.acc_name} ${it.acc_sum} ${it.acc_currency}" == selectedItem
        }
        return selectedClass!!.id
    }

    fun findWalletById(id: Int, walList: List<Wallet>): String {
        val selectedClass = walList.find {
            it.id == id
        }
        return "${selectedClass!!.acc_name} " +
                "${selectedClass.acc_sum} " +
                selectedClass.acc_currency
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    fun addWallet(accName: String, accCurrency: String, accSum: Float, accComment: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = walletRepository.addWallet(
                token,
                WalletRequestBody(
                    user = 1,
                    acc_name = accName,
                    acc_currency = accCurrency,
                    acc_sum = accSum,
                    acc_comment = accComment
                    )
            )
            withContext(Dispatchers.Main) {
                if (!response.isSuccessful) {
                    onError("Error : ${response.message()} ")
                } else {
                    getAllWallets()
                }
            }
        }
    }

    fun deleteWallet(id: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = walletRepository.deleteWallet(token = token, id = id)
            withContext(Dispatchers.Main) {
                if (!response.isSuccessful) {
                    onError("Error : ${response.message()} ")
                } else {
                    getAllWallets()
                }
            }
        }
    }

    fun editWallet(id: Int, accName: String, accCurrency: String, accSum: Float, accComment: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = walletRepository.editWallet(
                walletRequestBody = WalletRequestBody(
                    user = 1,
                    acc_name = accName,
                    acc_currency = accCurrency,
                    acc_sum = accSum,
                    acc_comment = accComment
                ),
                id = id
            )
            withContext(Dispatchers.Main) {
                if (!response.isSuccessful) {
                    onError("Error : ${response.message()} ")
                } else {
                    getAllWallets()
                }
            }
        }
    }
}
