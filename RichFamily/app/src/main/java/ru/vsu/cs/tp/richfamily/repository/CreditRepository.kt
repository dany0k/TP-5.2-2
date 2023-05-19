package ru.vsu.cs.tp.richfamily.repository

import okhttp3.ResponseBody
import retrofit2.Response
import ru.vsu.cs.tp.richfamily.api.model.credit.Credit
import ru.vsu.cs.tp.richfamily.api.model.credit.CreditRequestBody
import ru.vsu.cs.tp.richfamily.api.service.CreditApi

class CreditRepository(
    private val creditApi: CreditApi,
    private val token: String
) {
    suspend fun getAllCredits(): Response<List<Credit>> {
        return creditApi.getCredits(token = token)
    }

    suspend fun addCredit(
        token: String,
        creditRequestBody: CreditRequestBody
    ): Response<Credit> {
        return creditApi.addCredit(token = token, creditRequestBody = creditRequestBody)
    }

    suspend fun addCreditNotAuth(
        creditNotAuthRequestBody: CreditRequestBody
    ): Response<Credit> {
        return creditApi.addCreditNotAuth(creditNotAuthRequestBody = creditNotAuthRequestBody)
    }

    suspend fun deleteCredit(
        token: String,
        id: Int
    ) : Response<ResponseBody> {
        return creditApi.deleteCredit(token = token, id = id)
    }
}