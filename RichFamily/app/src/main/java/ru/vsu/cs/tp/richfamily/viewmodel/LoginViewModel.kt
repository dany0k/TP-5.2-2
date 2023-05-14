package ru.vsu.cs.tp.richfamily.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import ru.vsu.cs.tp.richfamily.api.model.auth.BaseResponse
import ru.vsu.cs.tp.richfamily.api.model.auth.LoginRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.User
import ru.vsu.cs.tp.richfamily.repository.UserRepository

class LoginViewModel(application: Application) :
    AndroidViewModel(application) {
    val token = MutableLiveData<String>()

    val userRepo = UserRepository()
    val loginResult: MutableLiveData<BaseResponse<User>> = MutableLiveData()
    val logoutResult: MutableLiveData<BaseResponse<ResponseBody>> = MutableLiveData()

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

    fun logoutUser(token: String) {
        logoutResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = userRepo.logoutUser(
                    token = token
                )
                if (response?.code() == 200) {
                    logoutResult.value =
                        BaseResponse.Success(response.body())
                } else {
                    logoutResult.value =
                        BaseResponse.Error(response?.message())
                }
            } catch (ex: java.lang.Exception) {
                logoutResult.value = BaseResponse.Error(ex.message)
            }
        }
    }
}