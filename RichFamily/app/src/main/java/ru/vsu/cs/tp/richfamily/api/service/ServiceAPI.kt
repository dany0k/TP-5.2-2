package ru.vsu.cs.tp.richfamily.api.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import ru.vsu.cs.tp.richfamily.api.model.*

interface ServiceAPI {

    @POST("auth/token/login")
    suspend fun auth(@Body authRequest: AuthRequest): Response<User>

    @Headers("Content-type: application/json")
    @GET("api/v1/categories")
    suspend fun getCategories(@Header("Authorization") token: String) : List<Category>

    @Headers("Content-type: application/json")
    @DELETE("api/v1/categories/{id}/")
    suspend fun deleteCategory(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ) : Response<ResponseBody>

    @Headers("Content-type: application/json")
    @POST("api/v1/categories/")
    suspend fun addCategory(
        @Header("Authorization") token: String,
        @Body categoryRequestBody: CategoryRequestBody
    ) : Response<Category>

    @Headers("Content-type: application/json")
    @PUT("api/v1/categories/{id}/")
    suspend fun updateCategory(
        @Header("Authorization") token: String,
        @Body categoryRequestBody: CategoryRequestBody,
        @Path("id") id: Int
    ) : Response<Category>

    @Headers("Content-type: application/json")
    @GET("api/v1/accounts")
    suspend fun getWallets(@Header("Authorization") token: String) : List<Wallet>

    @Headers("Content-type: application/json")
    @POST("api/v1/accounts/")
    suspend fun addWallet(
        @Header("Authorization") token: String,
        @Body walletRequestBody: WalletRequestBody
    ) : Response<Wallet>
}
