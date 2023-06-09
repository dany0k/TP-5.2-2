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
import ru.vsu.cs.tp.richfamily.databinding.FragmentRecoveryBinding
import ru.vsu.cs.tp.richfamily.utils.Constants
import ru.vsu.cs.tp.richfamily.utils.YandexEvents
import ru.vsu.cs.tp.richfamily.viewmodel.LoginViewModel

class RecoveryFragment : Fragment() {
    private lateinit var binding: FragmentRecoveryBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecoveryBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.resetResult.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading-> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    process()
                    showToast(Constants.SUCCESS_LOGIN)
                    stopAnim()
                }

                is BaseResponse.Error -> {
                    processError()
                    stopAnim()
                }
                else -> {
                    stopLoading()
                }
            }
        }
        binding.recoverButton.setOnClickListener {
            doReset()
        }
    }

    private fun doReset() {
        val email = binding.emailEt.text.toString()
        val secretWord = binding.secretWordEt.text.toString()
        val newPwd = binding.passwordEt.text.toString()
        val newPwdSub = binding.passwprdAgainEt.text.toString()
        binding.errorMessageTv.visibility = View.INVISIBLE
        if (inputCheck(email, secretWord, newPwd, newPwdSub)) {
            if (!viewModel.isValidEmail(email)) {
                binding.emailEt.error = Constants.INVALID_EMAIL
                return
            }
            if (!viewModel.comparePwd(newPwd, newPwdSub)) {
                binding.passwprdAgainEt.error = Constants.PWD_NOT_COMPARE
                binding.passwordEt.error = Constants.PWD_NOT_COMPARE
                return
            }
            if (!viewModel.isPwdValid(newPwd)) {
                binding.passwordEt.error = Constants.PWD_INVALID
                return
            }
            binding.recoverButton.startAnimation()
            viewModel.resetPwd(
                email = email,
                secretWord = secretWord,
                newPassword = newPwd
            )
            YandexMetrica.reportEvent(YandexEvents.USER_RESET_PWD)
        }
    }

    private fun stopAnim() {
        binding.recoverButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner)
        binding.recoverButton.revertAnimation()
    }

    private fun processError() {
        showErrMsg(Constants.INVALID_DATA)
    }

    private fun showErrMsg(msg: String) {
        binding.errorMessageTv.text = msg
        binding.errorMessageTv.visibility = View.VISIBLE
    }

    private fun process() {
        showToast(Constants.SUCCESS_LOGIN)
        navigateHome()
    }

    private fun navigateHome() {
        findNavController()
            .navigate(R.id.action_recoveryFragment_to_loginFragment)
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

    private fun inputCheck(
        email: String,
        secretWord: String,
        newPwd: String,
        newPwdSub: String
    ): Boolean {
        if (email.isBlank()) {
            binding.emailEt.error = Constants.COMP_FIELD
        }
        if (secretWord.isBlank()) {
            binding.secretWordEt.error = Constants.COMP_FIELD
        }
        if (newPwd.isBlank()) {
            binding.passwordEt.error = Constants.COMP_FIELD
        }
        if (newPwdSub.isBlank()) {
            binding.passwprdAgainEt.error = Constants.COMP_FIELD
        }
        return email.isNotBlank() && secretWord.isNotBlank() &&
                newPwd.isNotBlank() && newPwdSub.isNotBlank()
    }
}