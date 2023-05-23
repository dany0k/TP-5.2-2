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
import ru.vsu.cs.tp.richfamily.api.model.category.Category
import ru.vsu.cs.tp.richfamily.api.model.category.CategoryRequestBody
import ru.vsu.cs.tp.richfamily.api.service.CategoryApi
import ru.vsu.cs.tp.richfamily.repository.CategoryRepository

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CategoryRepositoryTest {

    lateinit var token: String
    lateinit var categoryRepository: CategoryRepository

    @Mock
    lateinit var categoryApi: CategoryApi

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        token = "token"
        categoryRepository = CategoryRepository(categoryApi, "token")
    }

    @Test
    fun `should get categories test and return true`() {
        runBlocking {
            Mockito.`when`(categoryApi.getCategories(token))
                .thenReturn(Response.success(listOf()))
            val response = categoryRepository.getAllCategories().isSuccessful
            Assert.assertTrue(response)
        }
    }

    @Test
    fun `should delete category test and return true`() {
        runBlocking {
            Mockito.`when`(categoryApi.deleteCategory(token, 0))
                .thenReturn(Response.success(null))
            val response = categoryRepository.deleteCategory(token, 0).isSuccessful
            Assert.assertTrue(response)
        }
    }

    @Test
    fun `should not delete category test and throws NullPointerException`() {
        runBlocking {
            Mockito.`when`(categoryApi.deleteCategory(token, 0))
                .thenThrow(NullPointerException::class.java)
            Assert.assertThrows(NullPointerException::class.java) {
                runBlocking {
                    categoryRepository.deleteCategory(token, 1).isSuccessful
                }
            }
        }
    }

    @Test
    fun `should edit category and return true`() {
        runBlocking {
            val categoryRequestBody = CategoryRequestBody(
                user = 1,
                cat_name = "cat1"
            )
            Mockito.`when`(categoryApi.updateCategory(token, categoryRequestBody, 0))
                .thenReturn(Response.success(Category(0, "catChanged")))
            val response = categoryRepository.editCategory(categoryRequestBody, 0).isSuccessful
            Assert.assertTrue(response)
        }
    }

    @Test
    fun `should add category and return true`() {
        runBlocking {
            val categoryRequestBody = CategoryRequestBody(
                user = 1,
                cat_name = "newCat"
            )
            Mockito.`when`(categoryApi.addCategory(token, categoryRequestBody))
                .thenReturn(Response.success(null))
            val response = categoryRepository.addCategory(token, categoryRequestBody).isSuccessful
            Assert.assertTrue(response)
        }
    }
}