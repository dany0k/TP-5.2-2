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
import com.yandex.metrica.YandexMetrica
import ru.vsu.cs.tp.richfamily.MainActivity
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.api.service.WalletApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentAddWalletBinding
import ru.vsu.cs.tp.richfamily.repository.WalletRepository
import ru.vsu.cs.tp.richfamily.utils.Constants
import ru.vsu.cs.tp.richfamily.utils.YandexEvents
import ru.vsu.cs.tp.richfamily.viewmodel.WalletViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.AnyViewModelFactory

class AddWalletFragment : Fragment() {
    private lateinit var binding: FragmentAddWalletBinding
    private lateinit var walletViewModel: WalletViewModel
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddWalletBinding.inflate(
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addWalletButton.setOnClickListener {
            processButton()
        }
    }

    private fun processButton() {
        val walletName = binding.walletNameEt.text.toString()
        val walletTotal = binding.totalEt.text.toString()
        val walletCurrency = "RUB"
        val walletComment = binding.walletCommentTil.editText?.text.toString()
        if (!walletViewModel.isScoreValid(walletTotal)) {
            showToast(Constants.WALLET_INVALID)
            stopAnim()
            return
        }
        if (inputCheck(walletName, walletTotal, walletComment)) {
            binding.addWalletButton.startAnimation()
            walletViewModel.addWallet(
                accName = walletName,
                accSum = walletTotal.toFloat(),
                accCurrency = walletCurrency,
                accComment = walletComment
            )
            YandexMetrica.reportEvent(YandexEvents.ADD_WALLET)
            findNavController().popBackStack()
        } else {
            showToast(Constants.COMP_FIELDS_TOAST)
        }
        stopAnim()
    }

    private fun stopAnim() {
        binding.addWalletButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner)
        binding.addWalletButton.revertAnimation()
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun inputCheck(
        walletName: String,
        walletTotal: String,
        walletComment: String
    ) : Boolean {
        return (walletName.isNotBlank() &&
                walletTotal.isNotBlank() &&
                walletComment.isNotBlank())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.addWalletButton.dispose()
    }
}