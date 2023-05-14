package ru.vsu.cs.tp.richfamily.repository

import okhttp3.ResponseBody
import retrofit2.Response
import ru.vsu.cs.tp.richfamily.api.model.auth.LoginRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.User
import ru.vsu.cs.tp.richfamily.api.service.UserApi

class UserRepository {
    suspend fun loginUser(loginRequest: LoginRequest): Response<User>? {
        return UserApi.getUserApi()?.loginUser(loginRequest = loginRequest)
    }

    suspend fun logoutUser(token: String): Response<ResponseBody>? {
        return UserApi.getUserApi()?.logoutUser(token = token)
    }
}