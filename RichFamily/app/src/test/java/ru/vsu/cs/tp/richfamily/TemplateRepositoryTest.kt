package ru.vsu.cs.tp.richfamily

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response
import ru.vsu.cs.tp.richfamily.api.model.Category
import ru.vsu.cs.tp.richfamily.api.model.template.Template
import ru.vsu.cs.tp.richfamily.api.model.template.TemplateRequestBody
import ru.vsu.cs.tp.richfamily.api.service.TemplateApi
import ru.vsu.cs.tp.richfamily.repository.TemplateRepository

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TemplateRepositoryTest {

    lateinit var token: String
    lateinit var templateRepository: TemplateRepository

    @Mock
    lateinit var templateApi: TemplateApi

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        token = "token"
        templateRepository = TemplateRepository(templateApi, "token")
    }

    @Test
    fun `should get template and return true`() {
        runBlocking {
            Mockito.`when`(templateApi.getTemplates(token))
                .thenReturn(Response.success(listOf()))
            val response = templateRepository.getAllTemplates().isSuccessful
            Assert.assertTrue(response)
        }
    }

    @Test
    fun `should delete template and return true`() {
        runBlocking {
            Mockito.`when`(templateApi.deleteTemplate(token, 0))
                .thenReturn(Response.success(null))
            val response = templateRepository.deleteTemplate(token, 0).isSuccessful
            Assert.assertTrue(response)
        }
    }

    @Test
    fun `should not delete template and throws NullPointerException`() {
        runBlocking {
            Mockito.`when`(templateApi.deleteTemplate(token, 0))
                .thenThrow(NullPointerException::class.java)
            Assert.assertThrows(NullPointerException::class.java) {
                runBlocking {
                    templateRepository.deleteTemplate(token, 1).isSuccessful
                }
            }
        }
    }

    @Test
    fun `should edit template and return true`() {
        runBlocking {
            val templateRequestBody = TemplateRequestBody(
                1, 1,
                "name1", "var1", "rec1", 4660.0F, ""
            )
            Mockito.`when`(templateApi.updateTemplate(token, templateRequestBody, 0))
                .thenReturn(Response.success(Template(1, 1, 1,
                    "name1", "var1", "rec1", 4660.0F, "")))
            val response = templateRepository.editTemplate(templateRequestBody, 0).isSuccessful
            Assert.assertTrue(response)
        }
    }

    @Test
    fun `should add template and return true`() {
        runBlocking {
            val templateRequestBody = TemplateRequestBody(
                1, 1,
                "name1", "var1", "rec1", 4660.0F, ""
            )
            Mockito.`when`(templateApi.addTemplate(token, templateRequestBody))
                .thenReturn(Response.success(null))
            val response = templateRepository.addTemplate(templateRequestBody).isSuccessful
            Assert.assertTrue(response)
        }
    }
}