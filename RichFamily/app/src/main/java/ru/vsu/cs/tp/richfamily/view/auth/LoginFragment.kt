package ru.vsu.cs.tp.richfamily.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.app.App
import ru.vsu.cs.tp.richfamily.databinding.FragmentLoginBinding
import ru.vsu.cs.tp.richfamily.api.model.AuthRequest
import ru.vsu.cs.tp.richfamily.viewmodel.LoginViewModel

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(
            layoutInflater,
            container,
            false
            )
        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory
                .getInstance(App()))[LoginViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.initRetrofit()
        binding.apply {
            loginButton.setOnClickListener {
                auth(
                    AuthRequest(
                        userEmailEt.text.toString(),
                        userPassEt.text.toString()
                    )
                )
                findNavController()
                    .navigate(R.id.action_loginFragment_to_walletFragment)
            }
            forgotPasswordButton.setOnClickListener {
                findNavController()
                    .navigate(R.id.action_loginFragment_to_recoveryFragment)
            }
        }
    }

    private fun auth(authRequest: AuthRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = App.serviceAPI.auth(authRequest)
            val message = response.errorBody()?.string()?.let {
                JSONObject(it).getString("message")
            }
            requireActivity().runOnUiThread {
                binding.errorTv.text = message
                val user = response.body()
                if (user != null) {
                    binding.errorTv.text = "Успешный вход"
                    viewModel.token.value = user.auth_token
                    viewModel.saveToken(user.auth_token)
                }
            }
        }
    }
}