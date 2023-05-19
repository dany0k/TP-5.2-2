package ru.vsu.cs.tp.richfamily.repository

import okhttp3.ResponseBody
import retrofit2.Response
import ru.vsu.cs.tp.richfamily.api.model.auth.LoginRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.User
import ru.vsu.cs.tp.richfamily.api.model.auth.UserProfile
import ru.vsu.cs.tp.richfamily.api.model.auth.UserRequestBody
import ru.vsu.cs.tp.richfamily.api.service.UserApi

class UserRepository {
    suspend fun loginUser(loginRequest: LoginRequest): Response<User>? {
        return UserApi.getUserApi()?.loginUser(loginRequest = loginRequest)
    }

    suspend fun logoutUser(token: String): Response<ResponseBody>? {
        return UserApi.getUserApi()?.logoutUser(token = token)
    }

    suspend fun getUserInfo(token: String): Response<UserProfile>? {
        return UserApi.getUserApi()?.getUser(token = token)
    }

    suspend fun updateUser(
        id: Int,
        token: String,
        userRequestBody: UserRequestBody
    ): Response<User>? {
        return UserApi.getUserApi()?.updateUser(
            id = id,
            userRequestBody = userRequestBody,
            token = token)
    }
}