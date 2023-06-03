package ru.vsu.cs.tp.richfamily.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import ru.vsu.cs.tp.richfamily.api.model.auth.BaseRegistrationRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.BaseResponse
import ru.vsu.cs.tp.richfamily.api.model.auth.LoginRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.RegisterRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.ResetPwdRequestBody
import ru.vsu.cs.tp.richfamily.api.model.auth.User
import ru.vsu.cs.tp.richfamily.api.model.auth.UserProfile
import ru.vsu.cs.tp.richfamily.api.model.auth.UserRequestBody
import ru.vsu.cs.tp.richfamily.repository.UserRepository

class LoginViewModel(application: Application) :
    AndroidViewModel(application) {
    val token = MutableLiveData<String>()

    private val userRepo = UserRepository()
    val loginResult: MutableLiveData<BaseResponse<User>> = MutableLiveData()
    val regResult: MutableLiveData<BaseResponse<User>> = MutableLiveData()
    val resetResult: MutableLiveData<BaseResponse<ResponseBody>> = MutableLiveData()
    val status: MutableLiveData<String> = MutableLiveData()
    private val logoutResult: MutableLiveData<BaseResponse<ResponseBody>> = MutableLiveData()
    val currentUser: MutableLiveData<UserProfile> = MutableLiveData()
    val errorMessage = MutableLiveData<String>()
    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

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
                        regResult.postValue(BaseResponse.Success(response.body()))
                    } else {
                        regResult.postValue(BaseResponse.Error(responseBase.message()))
                    }
                } else {
                    regResult.postValue(BaseResponse.Error(responseBase?.message()))
                }
            } catch (ex: java.lang.Exception) {
                regResult.value = BaseResponse.Error(ex.message)
            }
        }
    }

    fun getUserInformation(token: String) {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = userRepo.getUserInfo(token = token)
                withContext(Dispatchers.Main) {
                    if (response!!.code() == 200) {
                        currentUser.postValue(response.body())
                        loading.value = false
                    } else {
                        onError("Error : ${response.message()} ")
                    }
                }
            } catch (ex: Exception) {
                onError(ex.toString())
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
                    resetResult.postValue(BaseResponse.Success(response.body()))
                } else {
                    resetResult.postValue(BaseResponse.Error(response?.message()))
                }
            } catch (ex: java.lang.Exception) {
                resetResult.postValue(BaseResponse.Error(ex.message))
            }
        }
    }
    fun comparePwd(pwd: String, subPwd: String): Boolean {
        return pwd == subPwd
    }

    fun isPwdValid(pwd: String): Boolean {
        val regex = Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}\$")
        val matchResult = regex.find(pwd)
        return if (matchResult != null) {
            val matchedValue = matchResult.value
            true
        } else {
            false
        }
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun updateUserInformation(token: String, id: Int, firstname: String, lastname: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = userRepo.updateUser(
                    id = id,
                    token = token,
                    userRequestBody = UserRequestBody(
                        id = id,
                        first_name = firstname,
                        last_name = lastname
                    )
                )
                withContext(Dispatchers.Main) {
                    if (response!!.code() == 200) {
                        loading.value = false
                    } else {
                        onError("Error : ${response.message()} ")
                    }
                }
            } catch (ex: Exception) {
                onError(ex.toString())
            }
        }
    }

    fun getUserStatusOnboard(token: String) {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = userRepo.getUserInfo(token = token)
            withContext(Dispatchers.Main) {
                if (response!!.code() == 200) {
                    status.postValue(response.body().toString())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.postValue(message)
        loading.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}