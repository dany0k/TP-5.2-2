package ru.vsu.cs.tp.richfamily.api.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import ru.vsu.cs.tp.richfamily.api.model.wallet.Wallet
import ru.vsu.cs.tp.richfamily.api.model.wallet.WalletRequestBody

interface WalletApi {

    @Headers("Content-type: application/json")
    @GET("api/v1/accounts")
    suspend fun getWallets(@Header("Authorization") token: String) : Response<List<Wallet>>

    @Headers("Content-type: application/json")
    @DELETE("api/v1/accounts/{id}/")
    suspend fun deleteWallet(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ) : Response<ResponseBody>

    @Headers("Content-type: application/json")
    @POST("api/v1/accounts/")
    suspend fun addWallet(
        @Header("Authorization") token: String,
        @Body walletRequestBody: WalletRequestBody
    ) : Response<Wallet>

    @Headers("Content-type: application/json")
    @PUT("api/v1/accounts/{id}/")
    suspend fun updateWallet(
        @Header("Authorization") token: String,
        @Body walletRequestBody: WalletRequestBody,
        @Path("id") id: Int
    ) : Response<Wallet>

    companion object {
        fun getWalletApi() : WalletApi? {
            return ClientApi.client?.create(WalletApi::class.java)
        }
    }
}