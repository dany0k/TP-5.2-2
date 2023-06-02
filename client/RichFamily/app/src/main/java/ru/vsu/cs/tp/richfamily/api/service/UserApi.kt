package ru.vsu.cs.tp.richfamily.api.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import ru.vsu.cs.tp.richfamily.api.model.auth.BaseRegistrationRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.BaseUser
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.vsu.cs.tp.richfamily.api.model.auth.LoginRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.RegisterRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.ResetPwdRequestBody
import ru.vsu.cs.tp.richfamily.api.model.auth.User
import ru.vsu.cs.tp.richfamily.api.model.auth.UserProfile
import ru.vsu.cs.tp.richfamily.api.model.auth.UserRequestBody

interface UserApi {
    @POST("auth/token/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<User>

    @POST("auth/token/logout")
    suspend fun logoutUser(@Header("Authorization") token: String) : Response<ResponseBody>

    @POST("auth/utils/users/")
    suspend fun registerBase(
        @Body baseRegistrationRequest: BaseRegistrationRequest
    ) : Response<BaseUser>

    @POST("users/")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    ) : Response<User>

    @POST("auth/utils/users/reset_password/")
    suspend fun resetPwd(
        @Body resetPwdRequestBody: ResetPwdRequestBody
    ) : Response<ResponseBody>

    @GET("users/me")
    suspend fun getUser(@Header("Authorization") token: String): Response<UserProfile>

    @PUT("users/{id}/")
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