package ru.vsu.cs.tp.richfamily.repository

import okhttp3.ResponseBody
import retrofit2.Response
import ru.vsu.cs.tp.richfamily.api.model.auth.BaseRegistrationRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.BaseUser
import ru.vsu.cs.tp.richfamily.api.model.auth.LoginRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.RegisterRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.User
import ru.vsu.cs.tp.richfamily.api.service.UserApi

class UserRepository {
    suspend fun loginUser(loginRequest: LoginRequest): Response<User>? {
        return UserApi.getUserApi()?.loginUser(loginRequest = loginRequest)
    }

    suspend fun logoutUser(token: String): Response<ResponseBody>? {
        return UserApi.getUserApi()?.logoutUser(token = token)
    }

    suspend fun registerBase(
        baseRegistrationRequest: BaseRegistrationRequest
    ): Response<BaseUser>? {
        return UserApi.getUserApi()?.registerBase(
            baseRegistrationRequest = baseRegistrationRequest
        )
    }

    suspend fun registerUser(
        registrationRequest: RegisterRequest
    ): Response<User>? {
        return UserApi.getUserApi()?.registerUser(registerRequest = registrationRequest)
    }
}