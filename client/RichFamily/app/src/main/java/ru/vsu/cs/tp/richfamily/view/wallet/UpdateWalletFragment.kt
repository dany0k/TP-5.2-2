package ru.vsu.cs.tp.richfamily.view.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.vsu.cs.tp.richfamily.MainActivity
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.api.service.WalletApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentUpdateWalletBinding
import ru.vsu.cs.tp.richfamily.repository.WalletRepository
import ru.vsu.cs.tp.richfamily.utils.Constants
import ru.vsu.cs.tp.richfamily.utils.Filter
import ru.vsu.cs.tp.richfamily.viewmodel.WalletViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.AnyViewModelFactory
import java.text.DecimalFormat

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
        setWallet()
        binding.walletNameEt.filters = arrayOf(Filter.textFilter)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.updateWalletButton.setOnClickListener {
            updateItem()
            stopAnim()
        }
    }

    private fun setWallet() = with(binding) {
        walletNameEt.setText(args.wallet.acc_name)
        walletCommentTil.editText!!.setText(args.wallet.acc_comment)
        totalEt.setText(DecimalFormat("#.###").format(args.wallet.acc_sum))
    }

    private fun updateItem() = with(binding) {
        val walletName = walletNameEt.text.toString()
        val walletScore = totalEt.text.toString()
        val walletComment = walletCommentTil.editText!!.text.toString()
        if (!inputCheck(walletName, walletScore, walletComment)) {
            return@with
        }
        if (!walletViewModel.isScoreValid(walletScore)) {
            binding.totalEt.error = Constants.WALLET_INVALID
            return
        }
        binding.updateWalletButton.startAnimation()
        walletViewModel.editWallet(
            id = args.wallet.id,
            accName = walletName,
            accSum = walletScore.toFloat(),
            accCurrency = "RUB",
            accComment = walletComment
        )
        findNavController().popBackStack()
        showToast(Constants.WALLET_UPDATE)
    }

    private fun stopAnim() {
        binding.updateWalletButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner)
        binding.updateWalletButton.revertAnimation()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun inputCheck(
        walletTitle: String,
        walletScore: String,
        walletComment: String
    ) : Boolean {
        if (walletTitle.isBlank()) {
            binding.walletNameEt.error = Constants.COMP_FIELD
        }
        if (walletScore.isBlank()) {
            binding.totalEt.error = Constants.COMP_FIELD
        }
        if (walletComment.isBlank()) {
            binding.walletCommentTil.error = Constants.COMP_FIELD
        }
        if (walletTitle.length > 20) {
            binding.walletNameEt.error = Constants.MAX_LENGHT_ERR_20
        }
        if (walletScore.length > 9) {
            binding.totalEt.error = Constants.MAX_LENGHT_ERR_9
        }
        return (walletTitle.isNotBlank() &&
                walletScore.isNotBlank() &&
                walletComment.isNotBlank() &&
                walletTitle.length <= 20 &&
                walletScore.length <= 9)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.updateWalletButton.dispose()
    }
}