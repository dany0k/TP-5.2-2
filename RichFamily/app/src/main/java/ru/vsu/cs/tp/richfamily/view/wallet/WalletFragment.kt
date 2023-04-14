package ru.vsu.cs.tp.richfamily.view.wallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.WalletClickDeleteInterface
import ru.vsu.cs.tp.richfamily.adapter.WalletClickInterface
import ru.vsu.cs.tp.richfamily.adapter.WalletRVAdapter
import ru.vsu.cs.tp.richfamily.app.App
import ru.vsu.cs.tp.richfamily.databinding.FragmentWalletBinding
import ru.vsu.cs.tp.richfamily.model.Wallet
import ru.vsu.cs.tp.richfamily.viewmodel.WalletViewModel

class WalletFragment : Fragment(), WalletClickDeleteInterface, WalletClickInterface {

    private lateinit var binding: FragmentWalletBinding
    private lateinit var viewModel: WalletViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_wallet,
            container,
            false
        )
        binding.walletsRv.layoutManager = LinearLayoutManager(requireActivity())
        // Adapter
        val walletRVAdapter = WalletRVAdapter(
            this, this, this)
        binding.walletsRv.adapter = walletRVAdapter
        // ViewModel
        viewModel = ViewModelProvider(requireActivity(),
            ViewModelProvider.AndroidViewModelFactory
                .getInstance(App()))[WalletViewModel::class.java]
        viewModel.allWallets.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                walletRVAdapter.updateList(it)
            }
        })

        binding.fab.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_walletFragment_to_addWalletFragment)
        }
        return binding.root
    }

    override fun onDeleteIconClick(wallet: Wallet) {
        viewModel.deleteWallet(wallet)
    }

    override fun onNoteClick(wallet: Wallet) {
        viewModel.saveWallet(wallet)
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_walletFragment_to_addWalletFragment);
    }
}