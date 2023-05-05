package ru.vsu.cs.tp.richfamily.view.wallet

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.WalletRVAdapter
import ru.vsu.cs.tp.richfamily.api.service.WalletApi
import ru.vsu.cs.tp.richfamily.databinding.CategoryDialogBinding
import ru.vsu.cs.tp.richfamily.databinding.FragmentAddWalletBinding
import ru.vsu.cs.tp.richfamily.databinding.SubmitDialogBinding
import ru.vsu.cs.tp.richfamily.repository.WalletRepository
import ru.vsu.cs.tp.richfamily.utils.SessionManager
import ru.vsu.cs.tp.richfamily.viewmodel.WalletViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.WalletViewModelFactory

class AddWalletFragment : Fragment() {
    private lateinit var binding: FragmentAddWalletBinding
    private lateinit var walletViewModel: WalletViewModel
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddWalletBinding.inflate(
            inflater,
            container,
            false
        )
        token = try {
            SessionManager.getToken(requireActivity())!!
        } catch (e: java.lang.NullPointerException) {
            ""
        }
        if (token.isNotEmpty()) {
            val walletApi = WalletApi.getWalletApi()!!

            val walletRepository = WalletRepository(walletApi = walletApi, token = token)
            walletViewModel = ViewModelProvider(
                requireActivity(),
                WalletViewModelFactory(
                    walletRepository = walletRepository,
                    token = token
                )
            )[WalletViewModel::class.java]
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addWalletButton.setOnClickListener {
            val walletName = binding.walletNameEt.text.toString()
            val walletTotal = binding.totalEt.text.toString()
            val walletCurrency = "RUB"
            val walletComment = binding.walletCommentTil.editText?.text.toString()
            if (inputCheck(walletName, walletTotal, walletComment)) {
                walletViewModel.addWallet(
                    accName = walletName,
                    accSum = walletTotal.toFloat(),
                    accCurrency = walletCurrency,
                    accComment = walletComment
                )
                findNavController()
                    .navigate(R.id.action_addWalletFragment_to_walletFragment)
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Заполните все поля!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun inputCheck(
        walletName: String,
        walletTotal: String,
        walletComment: String
    ) : Boolean {
        return (walletName.isNotBlank() &&
                walletTotal.isNotBlank() &&
                walletComment.isNotBlank())
    }
}