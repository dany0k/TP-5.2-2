package ru.vsu.cs.tp.richfamily

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response
import ru.vsu.cs.tp.richfamily.api.model.template.Template
import ru.vsu.cs.tp.richfamily.api.service.TemplateApi
import ru.vsu.cs.tp.richfamily.repository.TemplateRepository
import ru.vsu.cs.tp.richfamily.viewmodel.TemplateViewModel

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TemplateViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    lateinit var templateViewModel: TemplateViewModel
    lateinit var templateRepository: TemplateRepository

    @Mock
    lateinit var templateApi: TemplateApi

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        templateRepository = TemplateRepository(templateApi, "token")
        templateViewModel = TemplateViewModel(templateRepository, "token")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get all template test`(){
        runBlocking {
            Mockito.`when`(templateRepository.getAllTemplates())
                .thenReturn(Response.success(listOf(
                    Template(1, 1, 1,
                        "name1", "var1", "rec1", 4660.0F, "")
                )))
            templateViewModel.getAllTemplates()
            val actual = templateViewModel.templatesList.getOrAwaitValue {}
            val expected = listOf(
                Template(1, 1, 1,
                    "name1", "var1", "rec1", 4660.0F, "")
            )
            Assert.assertEquals(expected, actual)
        }
    }
    @Test
    fun `empty template list test`() {
        runBlocking {
            Mockito.`when`(templateRepository.getAllTemplates())
                .thenReturn(Response.success(listOf()))
            templateViewModel.getAllTemplates()
            val actual = templateViewModel.templatesList.getOrAwaitValue()
            val expected = emptyList<Template>()
            Assert.assertEquals(expected, actual)
        }
    }
}