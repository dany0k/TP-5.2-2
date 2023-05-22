package ru.vsu.cs.tp.richfamily.view.account

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.vsu.cs.tp.richfamily.MainActivity
import ru.vsu.cs.tp.richfamily.SplashActivity
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
        token = MainActivity.getToken()
        binding = FragmentAccountBinding.inflate(
            inflater,
            container,
            false
        )
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
        Toast.makeText(requireContext(), Constants.SUCCESS, Toast.LENGTH_SHORT).show()
        val intent = Intent(activity, SplashActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}