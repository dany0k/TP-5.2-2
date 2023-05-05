package ru.vsu.cs.tp.richfamily.view.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.api.model.Wallet
import ru.vsu.cs.tp.richfamily.api.service.WalletApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentUpdateWalletBinding
import ru.vsu.cs.tp.richfamily.repository.WalletRepository
import ru.vsu.cs.tp.richfamily.utils.SessionManager
import ru.vsu.cs.tp.richfamily.viewmodel.WalletViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.WalletViewModelFactory

class UpdateWalletFragment : Fragment() {

    private val args by navArgs<UpdateWalletFragmentArgs>()
    private lateinit var binding: FragmentUpdateWalletBinding
    private lateinit var walletViewModel: WalletViewModel
    private lateinit var token: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateWalletBinding.inflate(
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
        setWallet()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.updateWalletButton.setOnClickListener {
            updateItem()
        }
    }

    private fun setWallet() = with(binding) {
        walletNameEt.setText(args.wallet.acc_name)
        walletCommentTil.editText!!.setText(args.wallet.acc_comment)
        totalEt.setText(args.wallet.acc_sum.toString())
    }

    private fun updateItem() = with(binding) {
        val walletName = walletNameEt.text.toString()
        val walletScore = totalEt.text.toString()
        val walletComment = walletCommentTil.editText!!.text.toString()
        if (inputCheck(walletName, walletScore, walletComment)) {
            walletViewModel.editWallet(
                id = args.wallet.id,
                accName = walletName,
                accSum = walletScore.toFloat(),
                accCurrency = "RUB",
                accComment = walletComment
            )
            findNavController()
                .navigate(R.id.action_updateWalletFragment_to_walletFragment)
            Toast.makeText(
                requireActivity(),
                "Счет изменен",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                requireActivity(),
                "Пожалуйста заполните все поля",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun inputCheck(
        walletTitle: String,
        walletScore: String,
        walletComment: String
    ) : Boolean {
        return (walletTitle.isNotBlank() &&
                walletScore.isNotBlank() &&
                walletComment.isNotBlank())
    }
}