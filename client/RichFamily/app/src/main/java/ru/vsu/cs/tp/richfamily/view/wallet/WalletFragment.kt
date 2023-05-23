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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.cs.tp.richfamily.MainActivity
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.WalletClickDeleteInterface
import ru.vsu.cs.tp.richfamily.adapter.WalletClickEditInterface
import ru.vsu.cs.tp.richfamily.adapter.WalletRVAdapter
import ru.vsu.cs.tp.richfamily.api.service.WalletApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentWalletBinding
import ru.vsu.cs.tp.richfamily.databinding.SubmitDialogBinding
import ru.vsu.cs.tp.richfamily.repository.WalletRepository
import ru.vsu.cs.tp.richfamily.viewmodel.WalletViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.AnyViewModelFactory

class WalletFragment :
    Fragment(),
    WalletClickDeleteInterface,
    WalletClickEditInterface {
    private lateinit var adapter: WalletRVAdapter
    private lateinit var binding: FragmentWalletBinding
    private lateinit var walletViewModel: WalletViewModel
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletBinding.inflate(
            layoutInflater,
            container,
            false
        )
        token = MainActivity.getToken()
        if (token.isNotEmpty()) {
            val walletApi = WalletApi.getWalletApi()!!
            val walletRepository = WalletRepository(walletApi = walletApi, token = token)
            walletViewModel = ViewModelProvider(
                requireActivity(),
                AnyViewModelFactory(
                    repository = walletRepository,
                    token = token
                )
            )[WalletViewModel::class.java]
        }
        initRcView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (token.isNotBlank()) {
            walletViewModel.walletList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
            walletViewModel.errorMessage.observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
            walletViewModel.loading.observe(viewLifecycleOwner) {
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
            walletViewModel.getAllWallets()
        }

        binding.addWalletFab.setOnClickListener {
            if (token.isEmpty()) {
                findNavController()
                    .navigate(R.id.action_walletFragment_to_registrationFragment)
            } else {
                findNavController()
                    .navigate(R.id.action_walletFragment_to_addWalletFragment)
            }
        }

        binding.walletsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    binding.addWalletFab.hide()
                } else if (dy < 0) {
                    binding.addWalletFab.show()
                }
            }
        })
    }

    private fun createDialog(id: Int) {
        val builder = AlertDialog.Builder(context)
        val dialogBinding = SubmitDialogBinding.inflate(
            layoutInflater,
            null,
            false
        )
        builder.setView(dialogBinding.root)
        val submitText: String = "При удалении счета, также удалятся все расходы и доходы, " +
                "записанные на этот счет.\nВы уверены?"
        dialogBinding.textToSubmit.text = submitText
        builder.setPositiveButton(R.string.accept) { _, _ ->
            walletViewModel.deleteWallet(id = id)
        }
        builder.setNegativeButton(R.string.cancel) { _, _ ->
            onDestroyView()
        }
        builder.show()
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
        createDialog(id)
        binding.addWalletFab.show()
    }

    override fun onEditIconClick(id: Int) {
        val action = WalletFragmentDirections
            .actionWalletFragmentToUpdateWalletFragment(adapter.currentList[id])
        findNavController()
            .navigate(action)
    }
}