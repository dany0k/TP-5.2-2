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
import ru.vsu.cs.tp.richfamily.api.model.Category
import ru.vsu.cs.tp.richfamily.api.model.CategoryRequestBody
import ru.vsu.cs.tp.richfamily.api.model.Wallet
import ru.vsu.cs.tp.richfamily.api.model.WalletRequestBody
import ru.vsu.cs.tp.richfamily.api.service.CategoryApi
import ru.vsu.cs.tp.richfamily.api.service.WalletApi
import ru.vsu.cs.tp.richfamily.repository.CategoryRepository
import ru.vsu.cs.tp.richfamily.repository.WalletRepository
import ru.vsu.cs.tp.richfamily.viewmodel.CategoryViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.WalletViewModel

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class WalletRepositoryTest {
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
    fun `should get wallet and return true`() {
        runBlocking {
            Mockito.`when`(walletApi.getWallets("token"))
                .thenReturn(Response.success(listOf()))
            val response = walletRepository.getAllWallets().isSuccessful
            Assert.assertTrue(response)
        }
    }

    @Test
    fun `should delete wallet and return true`() {
        runBlocking {
            Mockito.`when`(walletApi.deleteWallet("token", 0))
                .thenReturn(Response.success(null))
            val response = walletRepository.deleteWallet("token", 0).isSuccessful
            Assert.assertTrue(response)
        }
    }

    @Test
    fun `should not delete wallet and throws NullPointerException`() {
        runBlocking {
            Mockito.`when`(walletApi.deleteWallet("token", 0))
                .thenThrow(NullPointerException::class.java)
            Assert.assertThrows(NullPointerException::class.java) {
                runBlocking {
                    walletRepository.deleteWallet("token", 1).isSuccessful
                }
            }
        }
    }

    @Test
    fun `should edit wallet and return true`() {
        runBlocking {
            val walletRequestBody = WalletRequestBody(
                user = 1,
                acc_name = "acc1",
                acc_sum = 4660.0F,
                acc_currency = "RUB",
                acc_comment = ""
            )
            Mockito.`when`(walletApi.updateWallet("token", walletRequestBody, 0))
                .thenReturn(Response.success(Wallet(
                    id = 1,
                    user = 1,
                    acc_name = "acc1",
                    acc_sum = 4660.0F,
                    acc_currency = "RUB",
                    acc_comment = "")))
            val response = walletRepository.editWallet(walletRequestBody, 0).isSuccessful
            Assert.assertTrue(response)
        }
    }

    @Test
    fun `should add wallet and return true`() {
        runBlocking {
            val walletRequestBody = WalletRequestBody(
                user = 1,
                acc_name = "acc1",
                acc_sum = 4660.0F,
                acc_currency = "RUB",
                acc_comment = ""
            )
            Mockito.`when`(walletApi.addWallet("token", walletRequestBody))
                .thenReturn(Response.success(null))
            val response = walletRepository.addWallet("token", walletRequestBody).isSuccessful
            Assert.assertTrue(response)
        }
    }
}