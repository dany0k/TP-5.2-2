package ru.vsu.cs.tp.richfamily.view.wallet

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.databinding.FragmentUpdateWalletBinding
import ru.vsu.cs.tp.richfamily.model.Wallet
import ru.vsu.cs.tp.richfamily.viewmodel.WalletViewModel

class UpdateWalletFragment : Fragment() {

    private lateinit var binding: FragmentUpdateWalletBinding
    private lateinit var viewModel: WalletViewModel

    private val args by navArgs<UpdateWalletFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateWalletBinding.inflate(
            inflater,
            container,
            false
        )

        viewModel = ViewModelProvider(this)[WalletViewModel::class.java]

        binding.walletNameEt.setText(args.currentWallet.walletTitle)
        binding.totalEt.setText(args.currentWallet.walletScore.toString())
        binding.walletCommentTil.editText!!.setText(args.currentWallet.walletComment)

        binding.updateWalletButton.setOnClickListener {
            updateItem()
        }
        return binding.root
    }

    private fun updateItem() {
        val walletName = binding.walletNameEt.text.toString()
        val walletScore = Integer.parseInt(binding.totalEt.text.toString())
        val walletComment = binding.walletCommentTil.editText!!.text.toString()
        if (inputCheck(walletName, walletScore, walletComment)) {
            val updatedWallet = Wallet(
                args.currentWallet.id,
                walletName,
                walletScore,
                walletComment)
//            viewModel.updateWallet(updatedWallet)
            findNavController().navigate(R.id.action_updateWalletFragment_to_walletFragment)
            Toast.makeText(
                requireActivity(),
                "Счет изменен",
                Toast.LENGTH_LONG)
                .show()
        } else {
            Toast.makeText(
                requireActivity(),
                "Пожалуйста заполните все поля",
                Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun inputCheck(
        walletTitle: String,
        walletScore: Int,
        walletComment: String
    ) : Boolean {
        return !(TextUtils.isEmpty(walletTitle) &&
                TextUtils.isEmpty(walletComment) &&
                walletScore.toString().isEmpty())
    }
}