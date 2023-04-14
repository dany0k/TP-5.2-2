package ru.vsu.cs.tp.richfamily.view.wallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
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

        var walletTitle: String
        var walletScore: Int
        var walletComment: String

        viewModel.currentWallet().observe(viewLifecycleOwner) { wallet ->
            binding.walletNameEt.text.insert(0, wallet.walletTitle)
            binding.totalEt.text.insert(0, wallet.walletScore.toString())
            binding.walletCommentTil.editText?.text?.insert(0, wallet.walletComment)
            viewModel.clearCurrentWallet()
        }

        binding.addWalletButton.setOnClickListener {
            walletTitle = binding.walletNameEt.text.toString()
            walletScore = Integer.parseInt(binding.totalEt.text.toString())
            walletComment = binding.walletCommentTil.editText?.text.toString()
                if (walletTitle.isNotEmpty()) {
                    viewModel.addWallet(Wallet(walletTitle, walletScore, walletComment))
                    Toast.makeText(context, "Счет добавлен", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(it).navigate(R.id.action_addWalletFragment_to_walletFragment)
                } else {
                    Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_SHORT).show()
                }
            }
        return binding.root
    }
}