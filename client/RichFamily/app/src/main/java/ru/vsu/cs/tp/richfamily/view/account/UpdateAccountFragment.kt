package ru.vsu.cs.tp.richfamily.view.account

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.vsu.cs.tp.richfamily.MainActivity
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.databinding.FragmentUpdateAccountBinding
import ru.vsu.cs.tp.richfamily.utils.Constants
import ru.vsu.cs.tp.richfamily.utils.Filter
import ru.vsu.cs.tp.richfamily.viewmodel.LoginViewModel
import java.util.regex.Pattern

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
        token = MainActivity.getToken()
        binding.firstnameEt.filters = arrayOf(Filter.nameFilter)
        binding.lastnameEt.filters = arrayOf(Filter.nameFilter)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
                binding.content.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.content.visibility = View.VISIBLE
            }
        }
        setUser()
        binding.submitButton.setOnClickListener {
            binding.submitButton.startAnimation()
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
                findNavController().popBackStack(R.id.accountFragment, false)
            }
            binding.submitButton.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner)
            binding.submitButton.revertAnimation()
        }
    }

    private fun setUser() {
        binding.firstnameEt.setText(args.user.first_name)
        binding.lastnameEt.setText(args.user.last_name)
    }

    private fun inputCheck(firstname: String, lastname: String): Boolean {
        if (firstname.isBlank()) {
            binding.firstnameEt.error = Constants.COMP_FIELD
        }
        if (lastname.isBlank()) {
            binding.lastnameEt.error = Constants.COMP_FIELD
        }
        return firstname.isNotBlank() && lastname.isNotBlank()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.submitButton.dispose()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}