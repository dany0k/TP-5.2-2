package ru.vsu.cs.tp.richfamily.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.vsu.cs.tp.richfamily.repository.CategoryRepository
import ru.vsu.cs.tp.richfamily.repository.CreditRepository
import ru.vsu.cs.tp.richfamily.repository.WalletRepository
import ru.vsu.cs.tp.richfamily.viewmodel.CategoryViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.CreditViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.WalletViewModel

class AnyViewModelFactory(
    private val repository: Any,
    private val token: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CategoryViewModel::class.java) ->
                CategoryViewModel(
                    categoryRepository = repository as CategoryRepository,
                    token = token
                ) as T
            modelClass.isAssignableFrom(WalletViewModel::class.java) ->
                WalletViewModel(
                    walletRepository = repository as WalletRepository,
                    token = token
                ) as T
            modelClass.isAssignableFrom(CreditViewModel::class.java) ->
                CreditViewModel(
                    creditRepository = repository as CreditRepository,
                    token = token
                ) as T
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}