package ru.vsu.cs.tp.richfamily.view.account

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.databinding.FragmentAccountBinding
import ru.vsu.cs.tp.richfamily.utils.Constants
import ru.vsu.cs.tp.richfamily.utils.SessionManager
import ru.vsu.cs.tp.richfamily.viewmodel.LoginViewModel

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private val viewModel by viewModels<LoginViewModel>()

    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(
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
        if (token.isBlank()) {
            binding.nameTv.text = Constants.NAME_PLACEHOLDER
            binding.emailTv.text = Constants.EMAIL_PLACEHOLDER
            binding.editButton.visibility = View.INVISIBLE
            binding.exitButton.visibility = View.INVISIBLE
        } else {
            binding.editButton.visibility = View.VISIBLE
            binding.exitButton.visibility = View.VISIBLE
            viewModel.getUserInformation(token = token)
            viewModel.currentUser.observe(viewLifecycleOwner) {
                val fullName = "${it.first_name} ${it.last_name}";
                binding.nameTv.text = fullName
                binding.emailTv.text = it.email
            }
        }

        binding.editButton.setOnClickListener {
            val action = AccountFragmentDirections
                .actionAccountFragmentToUpdateAccountFragment(viewModel.currentUser.value!!)
            findNavController()
                .navigate(action)
        }

        binding.exitButton.setOnClickListener {
            doLogout()
        }
    }

    private fun doLogout() {
        viewModel.logoutUser(token = token)
        SessionManager.clearData(requireActivity())
        findNavController()
            .navigate(R.id.action_accountFragment_to_registrationFragment)
    }
}