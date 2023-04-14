package ru.vsu.cs.tp.richfamily.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vsu.cs.tp.richfamily.model.Wallet
import ru.vsu.cs.tp.richfamily.repository.WalletRepository
import ru.vsu.cs.tp.richfamily.room.AppDataBase

class WalletViewModel(application: Application) : AndroidViewModel(application) {
    // LiveData со списком всех счетов
    val allWallets: LiveData<List<Wallet>>
    private val repository: WalletRepository
    // MutableLiveData чтобы предзаполнять поля, при редактировании счета
    private var currWalletMutable = MutableLiveData<Wallet>()

    // Инициализируем репо и записываем все счета в список
    init {
        val dao = AppDataBase.getDatabase(application).getWalletsDao()
        repository = WalletRepository(dao)
        allWallets = repository.allWallets
    }

    fun deleteWallet(wallet: Wallet) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(wallet)
    }

    fun updateWallet(wallet: Wallet) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(wallet)
    }

    fun addWallet(wallet: Wallet) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(wallet)
    }

    fun saveWallet(wallet: Wallet) {
        currWalletMutable.value = wallet
    }

    // Для предзаполнения полей
    fun currentWallet(): LiveData<Wallet> {
        return currWalletMutable
    }

    // Очистка MutableLiveData после предзаполнения
    fun clearCurrentWallet() {
        currWalletMutable = MutableLiveData<Wallet>()
    }

}
