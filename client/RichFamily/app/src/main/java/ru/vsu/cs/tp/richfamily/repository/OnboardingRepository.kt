package ru.vsu.cs.tp.richfamily.repository

import retrofit2.Response
import ru.vsu.cs.tp.richfamily.api.model.onboarding.Onboarding
import ru.vsu.cs.tp.richfamily.api.service.OnboardingApi

class OnboardingRepository(
    private val onboardingApi: OnboardingApi
) {

    suspend fun getWelcomeOnboards() : Response<List<Onboarding>> {
        return onboardingApi.getOnboards(type = "hello")
    }
}