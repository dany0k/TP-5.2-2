package ru.vsu.cs.tp.richfamily.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.vsu.cs.tp.richfamily.api.model.Category
import ru.vsu.cs.tp.richfamily.api.model.CategoryRequestBody
import ru.vsu.cs.tp.richfamily.repository.CategoryRepository

class CategoryViewModel(
    private val categoryRepository: CategoryRepository,
    private val token: String
) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val catList = MutableLiveData<List<Category>>()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    fun getAllCategories() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = categoryRepository.getAllCategories()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    catList.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Ошибка : ${response.message()} ")
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

    fun addCategory(catName: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = categoryRepository.addCategory(
                token,
                CategoryRequestBody(cat_name = catName, user = 1))
            withContext(Dispatchers.Main) {
                if (!response.isSuccessful) {
                    onError("Ошибка : ${response.message()} ")
                } else {

                    getAllCategories()
                }
            }
        }
    }

    fun deleteCategory(id: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = categoryRepository.deleteCategory(token = token, id = id)
            withContext(Dispatchers.Main) {
                if (!response.isSuccessful) {
                    onError("Ошибка : ${response.message()} ")
                } else {
                    getAllCategories()
                }
            }
        }
    }

    fun editCategory(id: Int, catName: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = categoryRepository.editCategory(
                categoryRequestBody = CategoryRequestBody(user = 1, cat_name = catName),
                id = id)
            withContext(Dispatchers.Main) {
                if (!response.isSuccessful) {
                    onError("Ошибка : ${response.message()} ")
                } else {
                    getAllCategories()
                }
            }
        }
    }

    fun findCategoryById(id: Int, catList: List<Category>): String {
        val selectedClass = catList.find {
            it.id == id
        }
        return selectedClass!!.cat_name
    }

    fun getCategoryFromACTV(selectedItem: String, catList: List<Category>): Int {
        val selectedClass = catList.find {
            it.cat_name == selectedItem
        }
        return selectedClass!!.id
    }
}