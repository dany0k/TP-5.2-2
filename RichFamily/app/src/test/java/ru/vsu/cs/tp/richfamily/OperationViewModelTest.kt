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
import ru.vsu.cs.tp.richfamily.api.model.Operation
import ru.vsu.cs.tp.richfamily.api.service.OperationApi
import ru.vsu.cs.tp.richfamily.repository.CategoryRepository
import ru.vsu.cs.tp.richfamily.repository.OperationRepository
import ru.vsu.cs.tp.richfamily.viewmodel.OperationViewModel
import java.time.LocalDate
import java.time.LocalTime


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class OperationViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    lateinit var operationViewModel: OperationViewModel
    lateinit var operationRepository: OperationRepository

    @Mock
    lateinit var operationApi: OperationApi

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        operationRepository = OperationRepository(operationApi, "token")
        operationViewModel = OperationViewModel(operationRepository, "token")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get all operations test`(){
        runBlocking {
            `when`(operationRepository.getAllOperations())
                .thenReturn(Response.success(listOf(Operation(0, "var1", "payment", 4660.0F, LocalDate.parse("2023-01-03"), LocalTime.parse("13:30:00")))))
            operationViewModel.getAllOperations()
            val actual = operationViewModel.consList.getOrAwaitValue {}
            val expected = listOf(Operation(0, "var1", "payment", 4660.0F, LocalDate.parse("2023-01-03"), LocalTime.parse("13:30:00")))
            assertEquals(expected, actual)
        }
    }
    @Test
    fun `empty operation list test`() {
        runBlocking {
            `when`(operationRepository.getAllOperations())
                .thenReturn(Response.success(listOf()))
            operationViewModel.getAllOperations()
            val actual = operationViewModel.consList.getOrAwaitValue()
            val expected = emptyList<Operation>()
            assertEquals(expected, actual)
        }
    }
}