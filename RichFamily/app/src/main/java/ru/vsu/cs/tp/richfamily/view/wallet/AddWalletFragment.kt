package ru.vsu.cs.tp.richfamily.view.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.WalletRVAdapter
import ru.vsu.cs.tp.richfamily.api.model.Category
import ru.vsu.cs.tp.richfamily.api.model.Wallet
import ru.vsu.cs.tp.richfamily.api.model.WalletRequestBody
import ru.vsu.cs.tp.richfamily.app.App
import ru.vsu.cs.tp.richfamily.databinding.FragmentAddWalletBinding
import ru.vsu.cs.tp.richfamily.viewmodel.LoginViewModel

class AddWalletFragment : Fragment() {

    private lateinit var adapter: WalletRVAdapter
    private lateinit var binding: FragmentAddWalletBinding
    private lateinit var loginViewModel : LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddWalletBinding.inflate(
            inflater,
            container,
            false)
        loginViewModel = ViewModelProvider(requireActivity(),
            ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application))[LoginViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addWalletButton.setOnClickListener {
            val walletName = binding.walletNameEt.text.toString()
            val walletTotal = (binding.totalEt.text.toString()).toFloat()
            val walletCurrency = "RUB"
            val walletComment = binding.walletCommentTil.editText?.text.toString()
            addWallet(
                0,
                walletName,
                walletTotal,
                walletCurrency,
                walletComment)
            findNavController().navigate(R.id.action_addWalletFragment_to_walletFragment)
        }
    }


    private fun addWallet(
        user: Int,
        walletName: String,
        walletTotal: Float,
        walletCurrency: String,
        walletComment: String
    ) {
        if (walletName.isEmpty()) {
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            App.serviceAPI.addWallet(
                loginViewModel.token.value!!,
                WalletRequestBody(
                    user,
                    walletName,
                    walletTotal,
                    walletCurrency,
                    walletComment
                )
            )
            requireActivity().runOnUiThread {
                Toast.makeText(
                    requireActivity(),
                    R.string.succesful_add,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}