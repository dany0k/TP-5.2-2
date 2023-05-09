package ru.vsu.cs.tp.richfamily.repository

import okhttp3.ResponseBody
import retrofit2.Response
import ru.vsu.cs.tp.richfamily.api.model.operation.Operation
import ru.vsu.cs.tp.richfamily.api.model.operation.OperationRequestBody
import ru.vsu.cs.tp.richfamily.api.service.OperationApi

class OperationRepository(
    private val operationApi: OperationApi,
    private val token: String
) {

    suspend fun getAllOperations(): Response<List<Operation>> {
        return operationApi.getOperations(token = token)
    }

    suspend fun addOperation(operationRequestBody: OperationRequestBody
    ): Response<Operation> {
        return operationApi.addCategory(
            token = token,
            operationRequestBody = operationRequestBody
        )
    }

    suspend fun deleteOperation(token: String, id: Int): Response<ResponseBody> {
        return operationApi.deleteOperation(token = token, id = id)
    }

}