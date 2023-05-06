package ru.vsu.cs.tp.richfamily

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.Response
import ru.vsu.cs.tp.richfamily.api.model.Category
import ru.vsu.cs.tp.richfamily.api.model.CategoryRequestBody
import ru.vsu.cs.tp.richfamily.api.service.CategoryApi
import ru.vsu.cs.tp.richfamily.repository.CategoryRepository
import ru.vsu.cs.tp.richfamily.viewmodel.CategoryViewModel

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CategoryViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var categoryRepository: CategoryRepository

    @Mock
    lateinit var categoryApi: CategoryApi


    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        categoryRepository = CategoryRepository(categoryApi, "token")
        categoryViewModel = CategoryViewModel(categoryRepository, "token")
    }
    @Test
    fun `get all categories test`(){
        runBlocking {
            Mockito.`when`(categoryRepository.getAllCategories())
                .thenReturn(Response.success(listOf<Category>(Category(1, "newCategory"))))
            categoryViewModel.getAllCategories()
            val result = categoryViewModel.catList.getOrAwaitValue {}
            assertEquals(listOf<Category>(Category(1, "newCategory")), result)
        }
    }
    @Test
    fun `empty category list test`() {
        runBlocking {
            Mockito.`when`(categoryRepository.getAllCategories())
                .thenReturn(Response.success(listOf<Category>()))
            categoryViewModel.getAllCategories()
            val result = categoryViewModel.catList.getOrAwaitValue()
            assertEquals(listOf<Category>(), result)
        }
    }
    @Test
    fun `add new category test`(){
        runBlocking {
            Mockito.`when`(categoryRepository.addCategory(
                token = "token",
                categoryRequestBody = CategoryRequestBody(
                    user = 1,
                    cat_name = "cat1"
                )
            )).thenReturn(
                Response.success(
                    Category(id = 1, cat_name = "cat1")))
            categoryViewModel.catList.value= listOf(Category(id = 0, cat_name = "cat0"))
            categoryViewModel.addCategory(catName = "cat1")
            val result = categoryViewModel.catList.getOrAwaitValue {}
            assertEquals(listOf<Category>(Category(0, "cat0")), result)
        }
    }
    @Test
    fun `delete category test`(){
        runBlocking {
            categoryViewModel.catList.value= listOf(Category(id = 0, cat_name = "cat0"), Category(id = 1, cat_name = "cat1"))
            Mockito.`when`(categoryRepository.deleteCategory(
                token = "token",
                id = 0
            )).thenReturn(
                Response.success(null))

            categoryViewModel.deleteCategory(id = 1)
            val expectedList = listOf(Category(id = 0, cat_name = "cat0"))
            val result = categoryViewModel.catList.getOrAwaitValue {}
            assertEquals(expectedList, result)
        }
    }
    @Test
    fun testDeleteCategory() = runBlocking {
        val category1 = Category(0, "cat0")
        val category2 = Category(1, "cat1")
        val categories = listOf(category1, category2)
        categoryViewModel.catList.value = categories

        Mockito.`when`(categoryRepository.deleteCategory("token", category1.id)).thenReturn(Response.success(null))

        categoryViewModel.deleteCategory(category1.id)

        val expectedList = listOf(category2)
        assertEquals(expectedList, categoryViewModel.catList.value)
    }

    @Test
    fun testdeleteCategory() = runBlocking {
        // Arrange

        val id = 0
        val catList = listOf(Category(id = 0, cat_name = "cat0"), Category(id = 1, cat_name = "cat1"))

        // Replace categoryRepository with a fake object
        val fakeCategoryRepository = mock(CategoryRepository::class.java)
        Mockito.`when`(fakeCategoryRepository.deleteCategory("token", 0))
            .thenReturn(Response.success(null))

        // Update viewModel with the fake repository
        val viewModel = CategoryViewModel(fakeCategoryRepository,"token")
        viewModel.catList.value = catList

        // Act
        viewModel.deleteCategory(id)

        // Assert
        assertTrue(viewModel.catList.value?.none { it.id == id }!!)
    }
    @Test
    fun `edit category test`(){

    }
}