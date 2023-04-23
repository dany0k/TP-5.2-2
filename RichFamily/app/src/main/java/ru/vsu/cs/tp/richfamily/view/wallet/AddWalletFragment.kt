package ru.vsu.cs.tp.richfamily.view.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.vsu.cs.tp.richfamily.databinding.FragmentAddWalletBinding

class AddEditWalletFragment : Fragment() {

    private lateinit var binding: FragmentAddWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddWalletBinding.inflate(
            inflater,
            container,
            false)
        return binding.root
    }

//    private fun insertWalletToDatabase() {
//        val walletTitle: String = binding.walletNameEt.text.toString()
//        val walletScore: Int = Integer.parseInt(binding.totalEt.text.toString())
//        val walletComment: String = binding.walletCommentTil.editText!!.text.toString()
//
//        if (inputCheck(walletTitle, walletScore, walletComment)) {
////            val wallet = Wallet(0, walletTitle, walletScore, walletComment)
////            viewModel.addWallet(wallet)
//            Toast.makeText(
//                requireActivity(),
//                "Счет успешно добавлен",
//                Toast.LENGTH_LONG)
//                .show()
//            findNavController().navigate(R.id.action_addWalletFragment_to_walletFragment)
//        } else {
//            Toast.makeText(
//                requireActivity(),
//                "Пожалуйста заполните все поля",
//                Toast.LENGTH_LONG)
//                .show()
//        }
//    }
//
//    private fun inputCheck(
//        walletTitle: String,
//        walletScore: Int,
//        walletComment: String
//    ) : Boolean {
//        return !(TextUtils.isEmpty(walletTitle) &&
//                TextUtils.isEmpty(walletComment) &&
//                walletScore.toString().isEmpty())
//    }

}