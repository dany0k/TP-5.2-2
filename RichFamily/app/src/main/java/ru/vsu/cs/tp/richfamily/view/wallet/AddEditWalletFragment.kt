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
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.databinding.FragmentAddWalletBinding
import ru.vsu.cs.tp.richfamily.model.Wallet
import ru.vsu.cs.tp.richfamily.viewmodel.WalletViewModel

class AddEditWalletFragment : Fragment() {

    private lateinit var binding: FragmentAddWalletBinding
    private lateinit var viewModel: WalletViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddWalletBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[WalletViewModel::class.java]

        binding.addWalletButton.setOnClickListener {
            insertWalletToDatabase();
        }
        return binding.root
    }

    private fun insertWalletToDatabase() {
        val walletTitle: String = binding.walletNameEt.text.toString()
        val walletScore: Int = Integer.parseInt(binding.totalEt.text.toString())
        val walletComment: String = binding.walletCommentTil.editText!!.text.toString()

        if (inputCheck(walletTitle, walletScore, walletComment)) {
            val wallet = Wallet(0, walletTitle, walletScore, walletComment)
            viewModel.addWallet(wallet)
            Toast.makeText(
                requireActivity(),
                "Счет успешно добавлен",
                Toast.LENGTH_LONG)
                .show()
            findNavController().navigate(R.id.action_addWalletFragment_to_walletFragment)
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