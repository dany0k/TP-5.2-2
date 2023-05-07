package ru.vsu.cs.tp.richfamily.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.vsu.cs.tp.richfamily.repository.WalletRepository
import ru.vsu.cs.tp.richfamily.viewmodel.WalletViewModel

class WalletViewModelFactory(
    private val walletRepository: WalletRepository,
    private val token: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WalletViewModel::class.java)) {
            WalletViewModel(
                walletRepository = walletRepository,
                token = token
            ) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}