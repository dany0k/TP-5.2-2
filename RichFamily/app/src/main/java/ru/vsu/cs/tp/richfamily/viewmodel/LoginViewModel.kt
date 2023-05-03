package ru.vsu.cs.tp.richfamily.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.vsu.cs.tp.richfamily.utils.SharedPrefUtils

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    val token = MutableLiveData<String>()

    init {
        Log.d("AAAA", "View model created!")
        if (SharedPrefUtils.isEmpty(application.applicationContext)) {
            Log.d("AAAA", "no Data!")
            token.value = SharedPrefUtils
                .getToken(application.applicationContext)
            Log.d("AAAA", "${token.value}")

        } else {
            Log.d("AAAA", "data!")
        }
    }

    fun isToken(): Boolean {
        return token.value?.isNotEmpty() == true
    }

    fun saveToken(userToken: String) {
        token.value = userToken
        SharedPrefUtils.saveToken(
            getApplication<Application>().applicationContext, userToken
        )
    }
}