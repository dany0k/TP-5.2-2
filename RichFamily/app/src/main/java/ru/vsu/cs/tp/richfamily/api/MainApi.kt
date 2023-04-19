package ru.vsu.cs.tp.richfamily.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.vsu.cs.tp.richfamily.model.AuthRequest
import ru.vsu.cs.tp.richfamily.model.User

interface MainApi {

    @POST("auth/login")
    suspend fun auth(@Body authRequest: AuthRequest): Response<User>
}