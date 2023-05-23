package ru.vsu.cs.tp.richfamily.repository

import okhttp3.ResponseBody
import retrofit2.Response
import ru.vsu.cs.tp.richfamily.api.model.template.Template
import ru.vsu.cs.tp.richfamily.api.model.template.TemplateRequestBody
import ru.vsu.cs.tp.richfamily.api.service.TemplateApi

class TemplateRepository(
    private val templateApi: TemplateApi,
    private val token: String
) {
    suspend fun getAllTemplates(): Response<List<Template>> {
        return templateApi.getTemplates(token = token)
    }

    suspend fun addTemplate(templateRequestBody: TemplateRequestBody
    ): Response<Template> {
        return templateApi.addTemplate(
            token = token,
            templateRequestBody = templateRequestBody
        )
    }

    suspend fun deleteTemplate(token: String, id: Int): Response<ResponseBody> {
        return templateApi.deleteTemplate(token = token, id = id)
    }

    suspend fun editTemplate(
        templateRequestBody: TemplateRequestBody,
        id: Int
    ) : Response<Template> {
        return templateApi.updateTemplate(
            token = token,
            templateRequestBody = templateRequestBody,
            id = id
        )
    }
}