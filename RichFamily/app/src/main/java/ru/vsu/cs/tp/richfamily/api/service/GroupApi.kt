package ru.vsu.cs.tp.richfamily.api.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.vsu.cs.tp.richfamily.api.model.group.Group
import ru.vsu.cs.tp.richfamily.api.model.wallet.Wallet
import ru.vsu.cs.tp.richfamily.api.model.wallet.WalletRequestBody

interface GroupApi {

    @Headers("Content-type: application/json")
    @GET("api/v1/users/groups")
    suspend fun getUsersGroup(@Header("Authorization") token: String) : Response<List<Group>>

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
        fun getGroupApi() : GroupApi? {
            return ClientApi.client?.create(GroupApi::class.java)
        }
    }
}