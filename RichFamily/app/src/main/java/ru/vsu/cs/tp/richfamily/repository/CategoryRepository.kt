package ru.vsu.cs.tp.richfamily.repository

import okhttp3.ResponseBody
import retrofit2.Response
import ru.vsu.cs.tp.richfamily.api.model.Category
import ru.vsu.cs.tp.richfamily.api.model.CategoryRequestBody
import ru.vsu.cs.tp.richfamily.api.service.CategoryApi

class CategoryRepository(
    private val categoryApi: CategoryApi,
    private val token: String
) : MainRepository<Category> {

    override suspend fun getItem(): Response<List<Category>> {
        return categoryApi.getCategories(token = token)
    }

    suspend fun addCategory(
        token: String,
        categoryRequestBody: CategoryRequestBody
    ): Response<Category> {
        return categoryApi.addCategory(token = token, categoryRequestBody = categoryRequestBody)
    }

    suspend fun deleteCategory(
        token: String,
        id: Int
    ) : Response<ResponseBody> {
        return categoryApi.deleteCategory(token = token, id = id)
    }

    suspend fun editCategory(
        categoryRequestBody: CategoryRequestBody,
        id: Int
    ) : Response<Category> {
        return categoryApi.updateCategory(
            token = token,
            categoryRequestBody = categoryRequestBody,
            id = id
        )
    }
}