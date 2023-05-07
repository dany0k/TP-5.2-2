package ru.vsu.cs.tp.richfamily.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.vsu.cs.tp.richfamily.repository.CategoryRepository
import ru.vsu.cs.tp.richfamily.viewmodel.CategoryViewModel

class AnyViewModelFactory(
    private val repository: Any,
    private val token: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            CategoryViewModel(
                categoryRepository = repository as CategoryRepository,
                token = token
            ) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}