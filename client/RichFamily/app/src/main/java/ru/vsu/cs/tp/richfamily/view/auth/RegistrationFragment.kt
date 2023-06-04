package ru.vsu.cs.tp.richfamily.view.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yandex.metrica.YandexMetrica
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.api.model.auth.BaseResponse
import ru.vsu.cs.tp.richfamily.api.model.auth.User
import ru.vsu.cs.tp.richfamily.databinding.FragmentRegistrationBinding
import ru.vsu.cs.tp.richfamily.utils.Constants
import ru.vsu.cs.tp.richfamily.utils.SessionManager
import ru.vsu.cs.tp.richfamily.utils.YandexEvents
import ru.vsu.cs.tp.richfamily.viewmodel.LoginViewModel

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(
            layoutInflater,
            container,
            false
        )
        val token = SessionManager.getToken(requireActivity())
        if (!token.isNullOrBlank()) {
            navigateHome()
        }
        viewModel.regResult.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading-> {
                    showLoading()
                }
                is BaseResponse.Success -> {
                    processRegistration(it.data)
                    stopAnim()
                }
                is BaseResponse.Error -> {
                    stopAnim()
                }
                else -> {
                    stopLoading()
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.haveAccountButton.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }
        binding.regButton.setOnClickListener {
            doRegistration()
        }
    }

    private fun processRegistration(data: User?) {
        if (!data?.auth_token.isNullOrEmpty()) {
            data?.auth_token?.let {
                SessionManager.saveAuthToken(requireActivity(), it)
            }
            showToast(Constants.SUCCESS_LOGIN)
            YandexMetrica.reportEvent(YandexEvents.USER_REG)
            navigateHome()
        }
    }

    private fun doRegistration() {
        val email = binding.userEmailEt.text.toString()
        val pwd = binding.userPassEt.text.toString()
        val subPwd = binding.userSubmitPassEt.text.toString()
        val firstname = binding.userNameEt.text.toString()
        val lastname = binding.userSurnameEt.text.toString()
        val secretWord = binding.userSecretWordEt.text.toString()
        if (inputCheck(email, pwd, subPwd, firstname, lastname, secretWord)) {
            if (!viewModel.isPwdValid(pwd)) {
                showToast(Constants.PWD_INVALID)
                return
            }
            if (!viewModel.isValidEmail(email)) {
                showToast(Constants.INVALID_EMAIL)
                return
            }
            if (!viewModel.comparePwd(pwd, subPwd)) {
                showToast(Constants.PWD_NOT_COMPARE)
                return
            }
            binding.regButton.startAnimation()
            viewModel.registerUser(
                email = email,
                pwd = pwd,
                firstname = firstname,
                lastname = lastname,
                secretWord = secretWord
            )
        } else {
            showToast(Constants.COMP_FIELDS_TOAST)
        }
    }

    private fun stopAnim() {
        binding.regButton.revertAnimation()
        binding.regButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner)
    }

    private fun inputCheck(
        username: String,
        pwd: String,
        pwdAgain: String,
        firstname: String,
        lastname: String,
        secretWord: String
    ): Boolean {
        return username.isNotBlank() &&
                pwd.isNotBlank() &&
                pwdAgain.isNotBlank() &&
                firstname.isNotBlank() &&
                lastname.isNotBlank() &&
                secretWord.isNotBlank()
    }

    private fun navigateHome() {
        findNavController()
            .navigate(R.id.action_registrationFragment_to_walletFragment)
    }

    private fun processError(message: String) {
        showToast(message)
    }

    private fun stopLoading() { }

    private fun showLoading() { }

    private fun showToast(msg: String) {
        Toast.makeText(
            requireActivity(),
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.regButton.dispose()
    }
}