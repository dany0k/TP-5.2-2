package ru.vsu.cs.tp.richfamily.api.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.vsu.cs.tp.richfamily.api.model.auth.LoginRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.User

interface UserApi {
    @POST("auth/token/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<User>

    @POST("auth/token/logout")
    suspend fun logoutUser(@Header("Authorization") token: String) : Response<ResponseBody>

    companion object {
        fun getUserApi() : UserApi? {
            return ClientApi.client?.create(UserApi::class.java)
        }
    }
}