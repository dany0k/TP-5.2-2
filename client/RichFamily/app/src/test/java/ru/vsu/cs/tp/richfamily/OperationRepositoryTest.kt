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
import ru.vsu.cs.tp.richfamily.api.model.Operation
import ru.vsu.cs.tp.richfamily.api.model.operation.OperationRequestBody
import ru.vsu.cs.tp.richfamily.api.service.OperationApi
import ru.vsu.cs.tp.richfamily.repository.OperationRepository
import java.time.LocalDate
import java.time.LocalTime

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class OperationRepositoryTest {

    lateinit var token: String
    lateinit var operationRepository: OperationRepository

    @Mock
    lateinit var operationApi: OperationApi

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        token = "token"
        operationRepository = OperationRepository(operationApi, "token")
    }

    @Test
    fun `should get operation test and return true`() {
        runBlocking {
            Mockito.`when`(operationApi.getOperations(token))
                .thenReturn(Response.success(listOf()))
            val response = operationRepository.getAllOperations().isSuccessful
            Assert.assertTrue(response)
        }
    }

    @Test
    fun `should delete operation test and return true`() {
        runBlocking {
            Mockito.`when`(operationApi.deleteOperation(token, 0))
                .thenReturn(Response.success(null))
            val response = operationRepository.deleteOperation(token, 0).isSuccessful
            Assert.assertTrue(response)
        }
    }

    @Test
    fun `should not delete operation test and throws NullPointerException`() {
        runBlocking {
            Mockito.`when`(operationApi.deleteOperation(token, 0))
                .thenThrow(NullPointerException::class.java)
            Assert.assertThrows(NullPointerException::class.java) {
                runBlocking {
                    operationRepository.deleteOperation(token, 1).isSuccessful
                }
            }
        }
    }

    @Test
    fun `should edit operation and return true`() {
        runBlocking {
            val operationRequestBody = OperationRequestBody(
                account = 1,
                category = 1,
                op_variant = "var1",
                op_date = "01.03.2023",
                op_recipient = "rec1",
                op_sum = 4660.0F,
                op_comment = "bla bla bla"
            )
            Mockito.`when`(operationApi.updateOperation(token, operationRequestBody, 0))
                .thenReturn(Response.success(Operation(0, "var1", "payment", 4660.0F, LocalDate.parse("2023-01-03"), LocalTime.parse("13:30:00"))))
            val response = operationRepository.editOperation(operationRequestBody, 0).isSuccessful
            Assert.assertTrue(response)
        }
    }

    @Test
    fun `should add operation and return true`() {
        runBlocking {
            val operationRequestBody = OperationRequestBody(
                account = 1,
                category = 1,
                op_variant = "var1",
                op_date = "01.03.2023",
                op_recipient = "rec1",
                op_sum = 4660.0F,
                op_comment = "bla bla bla"
            )
            Mockito.`when`(operationApi.addOperation(token, operationRequestBody))
                .thenReturn(Response.success(null))
            val response = operationRepository.addOperation(operationRequestBody).isSuccessful
            Assert.assertTrue(response)
        }
    }
}