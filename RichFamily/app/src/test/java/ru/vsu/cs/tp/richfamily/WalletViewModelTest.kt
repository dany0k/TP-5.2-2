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
import ru.vsu.cs.tp.richfamily.api.model.wallet.Wallet
import ru.vsu.cs.tp.richfamily.api.service.WalletApi
import ru.vsu.cs.tp.richfamily.repository.WalletRepository
import ru.vsu.cs.tp.richfamily.viewmodel.WalletViewModel

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class WalletViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    lateinit var walletViewModel: WalletViewModel
    lateinit var walletRepository: WalletRepository

    @Mock
    lateinit var walletApi: WalletApi

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        walletRepository = WalletRepository(walletApi, "token")
        walletViewModel = WalletViewModel(walletRepository, "token")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get all wallet test`(){
        runBlocking {
            Mockito.`when`(walletRepository.getAllWallets())
                .thenReturn(Response.success(listOf(
                    Wallet(1, 1, "acc1",
                    4660.0F, "RUB", "")
                )))
            walletViewModel.getAllWallets()
            val actual = walletViewModel.walletList.getOrAwaitValue {}
            val expected = listOf(
                Wallet(1, 1, "acc1", 4660.0F,
                "RUB", "")
            )
            Assert.assertEquals(expected, actual)
        }
    }
    @Test
    fun `empty wallet list test`() {
        runBlocking {
            Mockito.`when`(walletRepository.getAllWallets())
                .thenReturn(Response.success(listOf()))
            walletViewModel.getAllWallets()
            val actual = walletViewModel.walletList.getOrAwaitValue()
            val expected = emptyList<Wallet>()
            Assert.assertEquals(expected, actual)
        }
    }
}