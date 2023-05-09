package ru.vsu.cs.tp.richfamily.repository

import retrofit2.Response
import ru.vsu.cs.tp.richfamily.api.model.credit.Credit
import ru.vsu.cs.tp.richfamily.api.service.CreditApi

class CreditRepository(
    private val creditApi: CreditApi,
    private val token: String
) {
    suspend fun getAllCredits(): Response<List<Credit>> {
        return creditApi.getCredits(token = token)
    }
}