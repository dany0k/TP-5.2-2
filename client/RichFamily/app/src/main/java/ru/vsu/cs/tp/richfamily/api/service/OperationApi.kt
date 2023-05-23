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
import ru.vsu.cs.tp.richfamily.api.model.operation.Operation
import ru.vsu.cs.tp.richfamily.api.model.operation.OperationRequestBody

interface OperationApi {

    @Headers("Content-type: application/json")
    @GET("api/v1/operations")
    suspend fun getOperations(@Header("Authorization") token: String) : Response<List<Operation>>

    @Headers("Content-type: application/json")
    @GET("api/v1/operations/{id}")
    suspend fun getOperationById(@Header("Authorization") token: String,
                                 @Path("id") id: Int
    ) : Response<Operation>
    @Headers("Content-type: application/json")
    @DELETE("api/v1/operations/{id}/")
    suspend fun deleteOperation(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ) : Response<ResponseBody>

    @Headers("Content-type: application/json")
    @POST("api/v1/operations/")
    suspend fun addOperation(
        @Header("Authorization") token: String,
        @Body operationRequestBody: OperationRequestBody
    ) : Response<Operation>

    @Headers("Content-type: application/json")
    @PUT("api/v1/operations/{id}/")
    suspend fun updateOperation(
        @Header("Authorization") token: String,
        @Body operationRequestBody: OperationRequestBody,
        @Path("id") id: Int
    ) : Response<Operation>

    companion object {
        fun getOperationApi() : OperationApi? {
            return ClientApi.client?.create(OperationApi::class.java)
        }
    }
}