package ru.vsu.cs.tp.richfamily.view.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.databinding.FragmentUpdateAccountBinding
import ru.vsu.cs.tp.richfamily.utils.Constants
import ru.vsu.cs.tp.richfamily.utils.SessionManager
import ru.vsu.cs.tp.richfamily.viewmodel.LoginViewModel

class UpdateAccountFragment : Fragment() {

    private lateinit var binding: FragmentUpdateAccountBinding
    private val viewModel by viewModels<LoginViewModel>()
    private val args by navArgs<UpdateAccountFragmentArgs>()

    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateAccountBinding.inflate(
            inflater,
            container,
            false
        )
        token = try {
            SessionManager.getToken(requireActivity())!!
        } catch (e: java.lang.NullPointerException) {
            ""
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUser()
        binding.submitButton.setOnClickListener {
        if (inputCheck(
                firstname = binding.firstnameEt.text.toString(),
                lastname = binding.lastnameEt.text.toString()
            )) {
            viewModel.updateUserInformation(
                token = token,
                id = args.user.id,
                firstname = binding.firstnameEt.text.toString(),
                lastname = binding.lastnameEt.text.toString()
            )
            Toast.makeText(
                requireActivity(),
                Constants.SUCCESS_TOAST,
                Toast.LENGTH_SHORT
            ).show()
            findNavController()
                .navigate(R.id.action_updateAccountFragment_to_accountFragment)
        } else {
            Toast.makeText(
                requireActivity(),
                Constants.COMP_FIELDS_TOAST,
                Toast.LENGTH_SHORT
            ).show()
            }
        }
    }

    private fun setUser() {
        binding.firstnameEt.setText(args.user.first_name)
        binding.lastnameEt.setText(args.user.last_name)
    }

    private fun inputCheck(firstname: String, lastname: String): Boolean {
        return firstname.isNotBlank() && lastname.isNotBlank()
    }
}