package ru.vsu.cs.tp.richfamily.view.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.*
import ru.vsu.cs.tp.richfamily.api.model.Wallet
import ru.vsu.cs.tp.richfamily.databinding.FragmentWalletBinding
import ru.vsu.cs.tp.richfamily.viewmodel.LoginViewModel

class WalletFragment :
    Fragment(),
    WalletClickDeleteInterface,
    WalletClickEditInterface {
    private lateinit var adapter: WalletRVAdapter
    private lateinit var loginViewModel : LoginViewModel
    private lateinit var binding: FragmentWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletBinding.inflate(
            layoutInflater,
            container,
            false
        )
        loginViewModel = ViewModelProvider(requireActivity(),
            ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application))[LoginViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()

        getWallets()

//        loginViewModel.token.observe(viewLifecycleOwner) {
//            loginViewModel.saveToken(it)
//        }

        binding.addWalletFab.setOnClickListener {
        }
    }

    private fun getWallets() {
        CoroutineScope(Dispatchers.IO).launch {
//            val list = App.serviceAPI.getWallets(loginViewModel.token.value!!)
            val list = mutableListOf(
                Wallet(0, 0, "Счет 1", 1000f, "RUB", "AAA"),
                Wallet(1, 0, "Счет 2", 10000f, "RUB", "AAA"),
            )
            requireActivity().runOnUiThread {
                adapter.submitList(list)
            }
        }
    }

    private fun initRcView() = with(binding) {
        adapter = WalletRVAdapter(
            this@WalletFragment,
            this@WalletFragment
        )
        walletsRv.layoutManager = LinearLayoutManager(context)
        walletsRv.adapter = adapter
    }

    override fun onDeleteIconClick(id: Int) {
//        CoroutineScope(Dispatchers.IO).launch {
//            val response = App.serviceAPI.deleteWallet(loginViewModel.token.value!!, id)
//            if (response.isSuccessful) {
//                requireActivity().runOnUiThread {
//                    Toast.makeText(
//                        requireActivity(),
//                        R.string.succesful_delete,
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    getWallets()
//                }
//            } else {
//                Toast.makeText(
//                    requireActivity(),
//                    R.string.error_delete,
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
    }

    override fun onEditIconClick(id: Int) {
        findNavController()
            .navigate(R.id.action_walletFragment_to_updateWalletFragment)
    }
}