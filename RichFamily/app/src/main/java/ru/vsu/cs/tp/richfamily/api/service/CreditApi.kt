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
import ru.vsu.cs.tp.richfamily.api.model.Category
import ru.vsu.cs.tp.richfamily.api.model.CategoryRequestBody
import ru.vsu.cs.tp.richfamily.api.model.credit.Credit

interface CreditApi {
    @Headers("Content-type: application/json")
    @GET("api/v1/credits")
    suspend fun getCredits(@Header("Authorization") token: String) : Response<List<Credit>>

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

    companion object {
        fun getCreditApi() : CreditApi? {
            return ClientApi.client?.create(CreditApi::class.java)
        }
    }
}