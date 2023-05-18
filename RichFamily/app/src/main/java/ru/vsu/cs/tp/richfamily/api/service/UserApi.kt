package ru.vsu.cs.tp.richfamily.api.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.vsu.cs.tp.richfamily.api.model.auth.BaseRegistrationRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.BaseUser
import ru.vsu.cs.tp.richfamily.api.model.auth.LoginRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.RegisterRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.User

interface UserApi {
    @POST("auth/token/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<User>

    @POST("auth/token/logout")
    suspend fun logoutUser(@Header("Authorization") token: String) : Response<ResponseBody>

    @POST("api/v1/auth/users/")
    suspend fun registerBase(
        @Body baseRegistrationRequest: BaseRegistrationRequest
    ) : Response<BaseUser>

    @POST("api/v1/users/")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    ) : Response<User>

    companion object {
        fun getUserApi() : UserApi? {
            return ClientApi.client?.create(UserApi::class.java)
        }
    }
}