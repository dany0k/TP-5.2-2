package ru.vsu.cs.tp.richfamily.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import ru.vsu.cs.tp.richfamily.api.model.auth.BaseRegistrationRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.BaseResponse
import ru.vsu.cs.tp.richfamily.api.model.auth.LoginRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.RegisterRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.ResetPwdRequestBody
import ru.vsu.cs.tp.richfamily.api.model.auth.User
import ru.vsu.cs.tp.richfamily.repository.UserRepository

class LoginViewModel(application: Application) :
    AndroidViewModel(application) {
    val token = MutableLiveData<String>()

    val userRepo = UserRepository()
    val loginResult: MutableLiveData<BaseResponse<User>> = MutableLiveData()
    val regResult: MutableLiveData<BaseResponse<User>> = MutableLiveData()
    val resetResult: MutableLiveData<BaseResponse<ResponseBody>> = MutableLiveData()
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

    fun registerUser(
        email: String,
        pwd: String,
        firstname: String,
        lastname: String,
        secretWord: String
    ) {
        regResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val registerBaseRequest = BaseRegistrationRequest(
                    username = email,
                    email = email,
                    password = pwd
                )
                val responseBase = userRepo.registerBase(
                    baseRegistrationRequest = registerBaseRequest
                )
                if (responseBase?.code() == 201) {
                    val registrationRequest = RegisterRequest(
                        user_id = responseBase.body()!!.id,
                        first_name = firstname,
                        last_name = lastname,
                        secret_word = secretWord
                    )
                    val response = userRepo.registerUser(
                        registrationRequest = registrationRequest
                    )
                    if (response?.code() == 200) {
                        regResult.value =
                            BaseResponse.Success(response.body())
                    } else {
                        loginResult.value =
                            BaseResponse.Error(responseBase.message())
                    }
                } else {
                    loginResult.value =
                        BaseResponse.Error(responseBase?.message())
                }
            } catch (ex: java.lang.Exception) {
                loginResult.value = BaseResponse.Error(ex.message)
            }
        }
    }

    fun resetPwd(email: String, secretWord: String, newPassword: String) {
        viewModelScope.launch {
            try {
                val resetPwdRequestBody = ResetPwdRequestBody(
                    email = email,
                    new_password = newPassword,
                    secret_word = secretWord
                )
                val response = userRepo.resetPwd(
                    resetPwdRequestBody = resetPwdRequestBody
                )
                if (response?.code() == 200) {
                    resetResult.value =
                        BaseResponse.Success(response.body())
                } else {
                    resetResult.value =
                        BaseResponse.Error(response?.message())
                }
            } catch (ex: java.lang.Exception) {
                resetResult.value = BaseResponse.Error(ex.message)
            }
        }
    }
    fun comparePwd(pwd: String, subPwd: String): Boolean {
        return pwd == subPwd
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}