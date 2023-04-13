package ru.vsu.cs.tp.richfamily.ui.wallet

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
        viewModel = ViewModelProvider(this)[WalletViewModel::class.java]
        var walletID: Int = -1

        binding.addWalletButton.setOnClickListener {
            val walletTitle: String = binding.walletNameEt.text.toString()
            val walletScore: Int = Integer.parseInt(binding.totalEt.text.toString())
            val walletComment: String = binding.walletCommentTil.toString()


            if (walletTitle.isNotEmpty()) {
                viewModel.addWallet(Wallet(walletTitle, walletScore))
                Toast.makeText(context, "Счет добавлен", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(it).navigate(R.id.action_addWalletFragment_to_walletFragment)
            } else {
                Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }
}