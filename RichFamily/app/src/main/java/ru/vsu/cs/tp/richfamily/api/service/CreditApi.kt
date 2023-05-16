package ru.vsu.cs.tp.richfamily.api.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import ru.vsu.cs.tp.richfamily.api.model.credit.Credit
import ru.vsu.cs.tp.richfamily.api.model.credit.CreditRequestBody

interface CreditApi {
    @Headers("Content-type: application/json")
    @GET("api/v1/credits")
    suspend fun getCredits(@Header("Authorization") token: String) : Response<List<Credit>>

    @Headers("Content-type: application/json")
    @DELETE("api/v1/credits/{id}/")
    suspend fun deleteCredit(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ) : Response<ResponseBody>

    @Headers("Content-type: application/json")
    @POST("api/v1/credits/")
    suspend fun addCredit(
        @Header("Authorization") token: String,
        @Body creditRequestBody: CreditRequestBody
    ) : Response<Credit>

    @Headers("Content-type: application/json")
    @POST("api/v1/credits/calc_credit/")
    suspend fun addCreditNotAuth(
        @Body creditNotAuthRequestBody: CreditRequestBody
    ) : Response<Credit>

    companion object {
        fun getCreditApi() : CreditApi? {
            return ClientApi.client?.create(CreditApi::class.java)
        }
    }
}