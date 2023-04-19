package ru.vsu.cs.tp.richfamily.view.wallet

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.WalletClickDeleteInterface
import ru.vsu.cs.tp.richfamily.adapter.WalletRVAdapter
import ru.vsu.cs.tp.richfamily.app.App
import ru.vsu.cs.tp.richfamily.databinding.FragmentWalletBinding
import ru.vsu.cs.tp.richfamily.model.Wallet
import ru.vsu.cs.tp.richfamily.room.AppDataBase
import ru.vsu.cs.tp.richfamily.viewmodel.LoginViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.WalletViewModel

class WalletFragment : Fragment(), WalletClickDeleteInterface {

    private lateinit var binding: FragmentWalletBinding
    private lateinit var viewModel: WalletViewModel

    private var userCount = 0

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
        val walletRVAdapter = WalletRVAdapter(this, this)
        binding.walletsRv.adapter = walletRVAdapter
        // ViewModel
        viewModel = ViewModelProvider(requireActivity(),
            ViewModelProvider.AndroidViewModelFactory
                .getInstance(App()))[WalletViewModel::class.java]
        CoroutineScope(Dispatchers.IO).launch {
            val dao = AppDataBase.getDatabase(requireActivity().applicationContext).getUserDao()
            userCount = dao.countUser()
        }

//        viewModel.allWallets.observe(viewLifecycleOwner, Observer { list ->
//            list?.let {
//                walletRVAdapter.updateList(it)
//            }
//        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setOnClickListener {
//            if (getToken().toString() == "") {
            if (userCount != 1) {
                findNavController()
                    .navigate(R.id.action_walletFragment_to_registrationFragment)
            } else {
                findNavController()
                    .navigate(R.id.action_walletFragment_to_addWalletFragment)
            }

        }

    }

    private fun getToken(): String? {
        val sharedPreferences = requireActivity().getSharedPreferences(
            "myPrefs", Context.MODE_PRIVATE
        )
        return sharedPreferences.getString("token", "")
    }

    // Нажатие на мусорку :-|
    override fun onDeleteIconClick(wallet: Wallet) {
//        viewModel.deleteWallet(wallet)
    }

}