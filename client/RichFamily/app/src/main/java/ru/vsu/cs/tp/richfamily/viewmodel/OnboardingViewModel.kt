package ru.vsu.cs.tp.richfamily.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vsu.cs.tp.richfamily.api.model.onboarding.Onboarding
import ru.vsu.cs.tp.richfamily.repository.OnboardingRepository

class OnboardingViewModel(
    private val onboardingRepository: OnboardingRepository,
    ) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val onbList = MutableLiveData<List<Onboarding>>()
    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    fun getWelcomeOnboards() {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = onboardingRepository.getWelcomeOnboards()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    onbList.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }
}