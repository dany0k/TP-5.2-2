package ru.vsu.cs.tp.richfamily.api.service

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.vsu.cs.tp.richfamily.api.model.LoginRequest
import ru.vsu.cs.tp.richfamily.api.model.User

interface UserApi {
    @POST("auth/token/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<User>

    companion object {
        fun getUserApi() : UserApi? {
            return ClientApi.client?.create(UserApi::class.java)
        }
    }
}