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
import com.yandex.metrica.impl.ob.Va
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.api.model.auth.BaseResponse
import ru.vsu.cs.tp.richfamily.api.model.auth.User
import ru.vsu.cs.tp.richfamily.databinding.FragmentRegistrationBinding
import ru.vsu.cs.tp.richfamily.utils.Constants
import ru.vsu.cs.tp.richfamily.utils.Filter
import ru.vsu.cs.tp.richfamily.utils.SessionManager
import ru.vsu.cs.tp.richfamily.utils.Validator
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
        binding.userNameEt.filters = arrayOf(Filter.nameFilter)
        binding.userSurnameEt.filters = arrayOf(Filter.nameFilter)
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
                    binding.errorMessageTv.visibility = View.GONE
                    stopAnim()
                }
                is BaseResponse.Error -> {
                    showErrMsg(Constants.USER_EXISTS)
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
        val firstname = binding.userNameEt.text.toString()
        val lastname = binding.userSurnameEt.text.toString()
        val secretWord = binding.userSecretWordEt.text.toString()
        binding.errorMessageTv.visibility = View.GONE
        if (!processValidation()) {
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
    }

    private fun processValidation(
    ): Boolean {
        return Validator.isValidFirstName(binding.userNameEt) &&
                Validator.isValidLastName(binding.userSurnameEt) &&
                Validator.isValidEmail(binding.userEmailEt) &&
                Validator.isValidPwd(binding.userPassEt) &&
                Validator.isValidSecretWord(binding.userSecretWordEt) &&
                Validator.comparePwd(binding.userPassEt, binding.userSubmitPassEt)
    }
    private fun stopAnim() {
        binding.regButton.revertAnimation()
        binding.regButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner)
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

    private fun showErrMsg(msg: String) {
        binding.errorMessageTv.text = msg
        binding.errorMessageTv.visibility = View.VISIBLE
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.regButton.dispose()
    }
}