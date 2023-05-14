package ru.vsu.cs.tp.richfamily.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.api.model.auth.BaseResponse
import ru.vsu.cs.tp.richfamily.api.model.auth.User
import ru.vsu.cs.tp.richfamily.databinding.FragmentLoginBinding
import ru.vsu.cs.tp.richfamily.utils.SessionManager
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
                    stopLoading()
                    processLogin(it.data)
                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                }
                else -> {
                    stopLoading()
                }
            }
        }
        binding.loginButton.setOnClickListener {
            doLogin()
        }
        return binding.root
    }

    private fun processLogin(data: User?) {
        showToast("Success:" + data?.auth_token)
        if (!data?.auth_token.isNullOrEmpty()) {
            data?.auth_token?.let {
                SessionManager.saveAuthToken(requireActivity(), it)
            }
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

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun stopLoading() { }

    private fun showLoading() { }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
