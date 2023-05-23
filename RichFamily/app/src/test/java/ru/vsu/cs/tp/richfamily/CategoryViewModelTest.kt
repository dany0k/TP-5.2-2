package ru.vsu.cs.tp.richfamily

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Response
import ru.vsu.cs.tp.richfamily.api.model.category.Category
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
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        categoryRepository = CategoryRepository(categoryApi, "token")
        categoryViewModel = CategoryViewModel(categoryRepository, "token")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get all categories test`(){
        runBlocking {
            `when`(categoryRepository.getAllCategories())
                .thenReturn(Response.success(listOf(Category(1, "newCategory"))))
            categoryViewModel.getAllCategories()
            val actual = categoryViewModel.catList.getOrAwaitValue {}
            val expected = listOf(Category(1, "newCategory"))
            assertEquals(expected, actual)
        }
    }
    @Test
    fun `empty category list test`() {
        runBlocking {
            `when`(categoryRepository.getAllCategories())
                .thenReturn(Response.success(listOf()))
            categoryViewModel.getAllCategories()
            val actual = categoryViewModel.catList.getOrAwaitValue()
            val expected = emptyList<Category>()
            assertEquals(expected, actual)
        }
    }
}