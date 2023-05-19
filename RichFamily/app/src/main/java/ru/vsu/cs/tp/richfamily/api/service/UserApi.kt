package ru.vsu.cs.tp.richfamily.api.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.vsu.cs.tp.richfamily.api.model.auth.LoginRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.User
import ru.vsu.cs.tp.richfamily.api.model.auth.UserProfile
import ru.vsu.cs.tp.richfamily.api.model.auth.UserRequestBody

interface UserApi {
    @POST("auth/token/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<User>

    @POST("auth/token/logout")
    suspend fun logoutUser(@Header("Authorization") token: String) : Response<ResponseBody>

    @GET("api/v1/users/me")
    suspend fun getUser(@Header("Authorization") token: String): Response<UserProfile>

    @PUT("api/v1/users/{id}/")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body userRequestBody: UserRequestBody
    ) : Response<User>

    companion object {
        fun getUserApi() : UserApi? {
            return ClientApi.client?.create(UserApi::class.java)
        }
    }
}