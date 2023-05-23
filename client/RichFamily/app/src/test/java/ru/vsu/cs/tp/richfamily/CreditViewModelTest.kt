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
import ru.vsu.cs.tp.richfamily.api.model.credit.Credit
import ru.vsu.cs.tp.richfamily.api.service.CreditApi
import ru.vsu.cs.tp.richfamily.repository.CreditRepository
import ru.vsu.cs.tp.richfamily.viewmodel.CreditViewModel

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CreditViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    lateinit var creditViewModel: CreditViewModel
    lateinit var creditRepository: CreditRepository

    @Mock
    lateinit var creditApi: CreditApi

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        creditRepository = CreditRepository(creditApi, "token")
        creditViewModel = CreditViewModel(creditRepository, "token")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get all credit test`(){
        runBlocking {
            Mockito.`when`(creditRepository.getAllCredits())
                .thenReturn(Response.success(listOf(
                    Credit(1, 1, "cr_name1",
                        4660.0F, 10, 12, 60.0F, 160.0F, 4820.0F)
                )))
            creditViewModel.getAllCredits()
            val actual = creditViewModel.creditList.getOrAwaitValue()
            val expected = listOf(
                Credit(1, 1, "cr_name1",
                    4660.0F, 10, 12, 60.0F, 160.0F, 4820.0F)
            )
            Assert.assertEquals(expected, actual)
        }
    }
    @Test
    fun `empty credit list test`() {
        runBlocking {
            Mockito.`when`(creditRepository.getAllCredits())
                .thenReturn(Response.success(listOf()))
            creditViewModel.getAllCredits()
            val actual = creditViewModel.creditList.getOrAwaitValue()
            val expected = emptyList<Credit>()
            Assert.assertEquals(expected, actual)
        }
    }
}