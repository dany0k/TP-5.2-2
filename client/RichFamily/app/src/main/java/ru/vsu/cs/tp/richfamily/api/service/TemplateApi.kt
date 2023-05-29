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
import ru.vsu.cs.tp.richfamily.api.model.template.Template
import ru.vsu.cs.tp.richfamily.api.model.template.TemplateRequestBody

interface TemplateApi {
    @Headers("Content-type: application/json")
    @GET("templates/")
    suspend fun getTemplates(@Header("Authorization") token: String) : Response<List<Template>>

    @Headers("Content-type: application/json")
    @DELETE("templates/{id}/")
    suspend fun deleteTemplate(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ) : Response<ResponseBody>

    @Headers("Content-type: application/json")
    @POST("templates/")
    suspend fun addTemplate(
        @Header("Authorization") token: String,
        @Body templateRequestBody: TemplateRequestBody
    ) : Response<Template>

    @Headers("Content-type: application/json")
    @PUT("templates/{id}/")
    suspend fun updateTemplate(
        @Header("Authorization") token: String,
        @Body templateRequestBody: TemplateRequestBody,
        @Path("id") id: Int
    ) : Response<Template>

    companion object {
        fun getTemplatesApi() : TemplateApi? {
            return ClientApi.client?.create(TemplateApi::class.java)
        }
    }
}