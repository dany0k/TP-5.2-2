package ru.vsu.cs.tp.richfamily.api.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ru.vsu.cs.tp.richfamily.api.model.onboarding.Onboarding

interface OnboardingApi {

    @Headers("Content-type: application/json")
    @GET("onboards/get_onboards_by_type/")
    suspend fun getOnboards(@Query("onboard_type") type: String) : Response<List<Onboarding>>

    companion object {
        fun getOnboardingApi() : OnboardingApi? {
            return ClientApi.client?.create(OnboardingApi::class.java)
        }
    }
}