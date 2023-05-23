package ru.vsu.cs.tp.richfamily

import android.app.Application
import androidx.lifecycle.Observer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import retrofit2.Response
import ru.vsu.cs.tp.richfamily.api.model.auth.BaseResponse
import ru.vsu.cs.tp.richfamily.api.model.auth.LoginRequest
import ru.vsu.cs.tp.richfamily.api.model.auth.User
import ru.vsu.cs.tp.richfamily.repository.UserRepository
import ru.vsu.cs.tp.richfamily.viewmodel.LoginViewModel

class LoginViewModelTest {

    @Mock
    lateinit var userRepo: UserRepository

    @Mock
    lateinit var observer: Observer<BaseResponse<User>>

    lateinit var viewModel: LoginViewModel

    @After
    fun tearDown() {
    }

    @Before
    fun setUp() {
        val application: Application = Mockito.mock(Application::class.java)
        viewModel = LoginViewModel(application)
        viewModel.loginResult.observeForever(observer)
    }

    @Test
    fun `should save and set value LOADING`() {
        viewModel.loginUser("user1", "test")
        val actual = viewModel.loginResult.value
        val expected = BaseResponse.Loading(nothing = null)
        Assert.assertEquals(actual, expected)
    }

    @Test
    suspend fun `should save and set value SUCCESS`() {
        val application: Application = Mockito.mock(Application::class.java)
        val viewModel = LoginViewModel(application)
        val expectedUser = User(auth_token = "00685552b503bafffa74855bcaa0dc157b12b569")
        val response = Response.success(expectedUser)
        val loginRequest = LoginRequest("user1", "Qwer123as")
        `when`(userRepo.loginUser(loginRequest = loginRequest)).thenReturn(response)
        viewModel.loginUser("user1", "Qwer123as")
        Assert.assertEquals(BaseResponse.Success(expectedUser), viewModel.loginResult.value)
    }

    @Test
    fun `should save and set value ERROR`() {
        val username = "testuser"
        val password = "testpassword"
        val expectedError = "Invalid credentials"
        viewModel.loginUser(username, password)
        Assert.assertEquals(BaseResponse.Error(expectedError), viewModel.loginResult.value)
    }
}