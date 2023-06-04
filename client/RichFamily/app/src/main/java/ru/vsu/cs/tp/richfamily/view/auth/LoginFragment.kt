package ru.vsu.cs.tp.richfamily.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yandex.metrica.YandexMetrica
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.api.model.auth.BaseResponse
import ru.vsu.cs.tp.richfamily.api.model.auth.User
import ru.vsu.cs.tp.richfamily.databinding.FragmentLoginBinding
import ru.vsu.cs.tp.richfamily.utils.Constants
import ru.vsu.cs.tp.richfamily.utils.SessionManager
import ru.vsu.cs.tp.richfamily.utils.YandexEvents
import ru.vsu.cs.tp.richfamily.viewmodel.LoginViewModel

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(
            layoutInflater,
            container,
            false
        )
        val token = SessionManager.getToken(requireActivity())
        if (!token.isNullOrBlank()) {
            navigateHome()
        }

        viewModel.loginResult.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading-> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    processLogin(it.data)
                }

                is BaseResponse.Error -> {
                    processError(it.msg!!)
                    binding.loginButton.revertAnimation()
                    binding.loginButton.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner)
                }
                else -> { }
            }
        }

        binding.loginButton.setOnClickListener {
            doLogin()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.forgotPasswordButton.setOnClickListener {
            findNavController()
                .navigate(R.id.action_loginFragment_to_recoveryFragment)
        }
    }

    private fun processLogin(data: User?) {
        showToast(Constants.SUCCESS)
        binding.loginButton.startAnimation()
        if (!data?.auth_token.isNullOrEmpty()) {
            data?.auth_token?.let {
                SessionManager.saveAuthToken(requireActivity(), it)
            }
            binding.loginButton.revertAnimation()
            YandexMetrica.reportEvent(YandexEvents.USER_LOGIN)
            navigateHome()
        }
    }

    private fun doLogin() {
        val username = binding.userEmailEt.text.toString()
        val pwd = binding.userPassEt.text.toString()
        viewModel.loginUser(
            username = username,
            pwd = pwd
        )
    }

    private fun processError(message: String) {
        showToast(message)
    }


    private fun showLoading() {
        binding.loginButton.startAnimation()
    }

    private fun navigateHome() {
        findNavController()
            .navigate(R.id.action_loginFragment_to_walletFragment)
    }

    private fun showToast(msg: String) {
        Toast.makeText(
            requireActivity(),
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }
}
