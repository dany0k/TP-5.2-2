package ru.vsu.cs.tp.richfamily.repository

import retrofit2.Response
import ru.vsu.cs.tp.richfamily.api.model.LoginRequest
import ru.vsu.cs.tp.richfamily.api.model.User
import ru.vsu.cs.tp.richfamily.api.service.UserApi

class UserRepository {
    suspend fun loginUser(loginRequest: LoginRequest): Response<User>? {
        return UserApi.getUserApi()?.loginUser(loginRequest)
    }
}