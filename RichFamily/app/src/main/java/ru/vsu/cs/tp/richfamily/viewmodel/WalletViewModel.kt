package ru.vsu.cs.tp.richfamily.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vsu.cs.tp.richfamily.model.Wallet
import ru.vsu.cs.tp.richfamily.repository.WalletRepository
import ru.vsu.cs.tp.richfamily.room.AppDataBase

class WalletViewModel(application: Application) : AndroidViewModel(application) {
    val allWallets: LiveData<List<Wallet>>
    private val repository: WalletRepository

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
}
