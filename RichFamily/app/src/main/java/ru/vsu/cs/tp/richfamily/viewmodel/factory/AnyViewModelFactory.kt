package ru.vsu.cs.tp.richfamily.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.vsu.cs.tp.richfamily.repository.CategoryRepository
import ru.vsu.cs.tp.richfamily.repository.OperationRepository
import ru.vsu.cs.tp.richfamily.repository.TemplateRepository
import ru.vsu.cs.tp.richfamily.repository.WalletRepository
import ru.vsu.cs.tp.richfamily.viewmodel.CategoryViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.OperationViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.TemplateViewModel
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
            modelClass.isAssignableFrom(OperationViewModel::class.java) ->
                OperationViewModel(
                    operationRepository = repository as OperationRepository,
                    token = token
                ) as T
            modelClass.isAssignableFrom(TemplateViewModel::class.java) ->
                TemplateViewModel(
                    templateRepository = repository as TemplateRepository,
                    token = token
                ) as T
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}