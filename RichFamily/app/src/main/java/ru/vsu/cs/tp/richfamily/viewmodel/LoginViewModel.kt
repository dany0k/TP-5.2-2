package ru.vsu.cs.tp.richfamily.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vsu.cs.tp.richfamily.api.model.BaseResponse
import ru.vsu.cs.tp.richfamily.api.model.LoginRequest
import ru.vsu.cs.tp.richfamily.api.model.User
import ru.vsu.cs.tp.richfamily.repository.UserRepository

class LoginViewModel(application: Application) :
    AndroidViewModel(application) {
    val token = MutableLiveData<String>()

    val userRepo = UserRepository()
    val loginResult: MutableLiveData<BaseResponse<User>> = MutableLiveData()

    fun loginUser(username: String, pwd: String) {
        loginResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(
                    username = username,
                    password = pwd
                )
                val response = userRepo.loginUser(
                    loginRequest = loginRequest
                )
                if (response?.code() == 200) {
                    loginResult.value =
                        BaseResponse.Success(response.body())
                } else {
                    loginResult.value =
                        BaseResponse.Error(response?.message())
                }
            } catch (ex: java.lang.Exception) {
                loginResult.value = BaseResponse.Error(ex.message)
            }
        }
    }
}