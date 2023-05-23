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
import ru.vsu.cs.tp.richfamily.api.model.credit.Credit
import ru.vsu.cs.tp.richfamily.api.model.credit.CreditRequestBody
import ru.vsu.cs.tp.richfamily.api.service.CreditApi
import ru.vsu.cs.tp.richfamily.repository.CreditRepository

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CreditRepositoryTest {

    lateinit var token: String
    lateinit var creditRepository: CreditRepository

    @Mock
    lateinit var creditApi: CreditApi

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        token = "token"
        creditRepository = CreditRepository(creditApi, "token")
    }

    @Test
    fun `should get credit and return true`() {
        runBlocking {
            Mockito.`when`(creditApi.getCredits(token))
                .thenReturn(Response.success(listOf()))
            val response = creditRepository.getAllCredits().isSuccessful
            Assert.assertTrue(response)
        }
    }

    @Test
    fun `should delete credit and return true`() {
        runBlocking {
            Mockito.`when`(creditApi.deleteCredit(token, 0))
                .thenReturn(Response.success(null))
            val response = creditRepository.deleteCredit(token, 0).isSuccessful
            Assert.assertTrue(response)
        }
    }

    @Test
    fun `should not delete credit and throws NullPointerException`() {
        runBlocking {
            Mockito.`when`(creditApi.deleteCredit(token, 0))
                .thenThrow(NullPointerException::class.java)
            Assert.assertThrows(NullPointerException::class.java) {
                runBlocking {
                    creditRepository.deleteCredit(token, 1).isSuccessful
                }
            }
        }
    }

    @Test
    fun `should add credit and return true`() {
        runBlocking {
            val creditRequestBody = CreditRequestBody(
                1, "cr_name1",
                4660.0F, 10, 12, 60.0F, 120.0F, 4780.0F
            )
            Mockito.`when`(creditApi.addCredit(token, creditRequestBody))
                .thenReturn(Response.success(Credit(1, 1, "cr_name1",
                    4660.0F, 10, 12, 60.0F, 120.0F, 4780.0F)))
            val response = creditRepository.addCredit(token, creditRequestBody).isSuccessful
            Assert.assertTrue(response)
        }
    }

    @Test
    fun `should not add credit and return null`() {
        runBlocking {
            val creditRequestBody = CreditRequestBody(
                1, "cr_name1",
                4660.0F, 10, 12, 60.0F, 120.0F, 4780.0F
            )
            Mockito.`when`(creditApi.addCredit(token, creditRequestBody))
                .thenReturn(Response.success(null))
            val response = creditRepository.addCredit(token, creditRequestBody).isSuccessful
            Assert.assertTrue(response)
        }
    }

    @Test
    fun `should add credit not auth and return true`() {
        runBlocking {
            val creditRequestBody = CreditRequestBody(
                1, "cr_name1",
                4660.0F, 10, 12, 60.0F, 120.0F, 4780.0F
            )
            Mockito.`when`(creditApi.addCreditNotAuth(creditRequestBody))
                .thenReturn(Response.success(null))
            val response = creditRepository.addCreditNotAuth(creditRequestBody).isSuccessful
            Assert.assertTrue(response)
        }
    }
}