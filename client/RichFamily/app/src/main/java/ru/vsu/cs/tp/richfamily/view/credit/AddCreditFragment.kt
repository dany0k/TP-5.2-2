package ru.vsu.cs.tp.richfamily.view.credit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.yandex.metrica.YandexMetrica
import ru.vsu.cs.tp.richfamily.MainActivity
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.api.service.CreditApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentAddCreditBinding
import ru.vsu.cs.tp.richfamily.repository.CreditRepository
import ru.vsu.cs.tp.richfamily.utils.Constants
import ru.vsu.cs.tp.richfamily.utils.Filter
import ru.vsu.cs.tp.richfamily.utils.YandexEvents
import ru.vsu.cs.tp.richfamily.viewmodel.CreditViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.AnyViewModelFactory

class AddCreditFragment : Fragment() {

    private lateinit var binding: FragmentAddCreditBinding
    private lateinit var creditViewModel: CreditViewModel
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCreditBinding.inflate(
            inflater,
            container,
            false
        )
        token = MainActivity.getToken()
        val creditApi = CreditApi.getCreditApi()!!

        val creditRepository = CreditRepository(creditApi = creditApi, token = token)
        creditViewModel = ViewModelProvider(
            this,
            AnyViewModelFactory(
                repository = creditRepository,
                token = token
            )
        )[CreditViewModel::class.java]
        binding.crNameEt.filters = arrayOf(Filter.textFilter)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.calculateCreditButton.setOnClickListener {
            YandexMetrica.reportEvent(YandexEvents.ADD_CREDIT)
            processButton()
        }
    }

    private fun processButton() {
        val crName = binding.crNameEt.text.toString()
        val crAllSum = binding.crSumEt.text.toString()
        val crMonthPay = 0F
        val crPerc = binding.crPercEt.text.toString()
        val crPercSum = 0F
        val crPeriod = binding.crPerionEt.text.toString()
        val crFirstPay = binding.crFirstPayEt.text.toString()
        val crTotalSum = 0F
        if (!inputCheck(crName, crAllSum, crPerc, crPeriod, crFirstPay)) {
            return
        }
        if (!checkSum(crFirstPay = crFirstPay.toFloat(), crAllSum = crAllSum.toFloat())) {
            binding.crFirstPayEt.error = Constants.FIRSTPAY_BT_SUM
            return
        }
        if (!checkFields(
                crPerc.toInt(),
                crFirstPay.toFloat(),
                crAllSum.toFloat(),
                crPeriod.toInt())
            ) {
            return
        }
        binding.calculateCreditButton.startAnimation()
        if (token.isNotBlank()) {
            processCreditAuth(
                crName, crAllSum.toFloat(), crMonthPay, crPerc.toInt(),
                crPercSum, crPeriod.toInt(), crFirstPay.toFloat(), crTotalSum)
        } else {
            processCreditNotAuth(
                crName, crAllSum.toFloat(), crPerc.toInt(),
                crPeriod.toInt(), crFirstPay.toFloat())
        }
        stopAnim()
    }

    private fun checkFields(crPerc: Int,
                            crFirstPay: Float,
                            crAllSum: Float,
                            crPeriod: Int
    ): Boolean {
        if (crPerc <= 0) {
            binding.crPercEt.error = Constants.INVALID_DATA
        }
        if (crFirstPay <= 0) {
            binding.crFirstPayEt.error = Constants.INVALID_DATA
        }
        if (crAllSum <= 0) {
            binding.crSumEt.error = Constants.INVALID_DATA
        }
        if (crPeriod <= 0) {
            binding.crPerionEt.error = Constants.INVALID_DATA
        }
        return crPerc > 0 && crFirstPay > 0 && crAllSum > 0 && crPeriod > 0
    }

    private fun checkSum(crAllSum: Float, crFirstPay: Float) : Boolean {
        return crFirstPay < crAllSum
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun inputCheck(
        crName: String,
        crAllSum: String,
        crPerc: String,
        crPeriod: String,
        crFirstPay: String,
    ): Boolean {
        if (crName.isBlank()) {
            binding.crNameEt.error = Constants.COMP_FIELD
        }
        if (crAllSum.isBlank()) {
            binding.crSumEt.error = Constants.COMP_FIELD
        }
        if (crPerc.isBlank()) {
            binding.crPercEt.error = Constants.COMP_FIELD
        }
        if (crPeriod.isBlank()) {
            binding.crPerionEt.error = Constants.COMP_FIELD
        }
        if (crFirstPay.isBlank()) {
            binding.crFirstPayEt.error = Constants.COMP_FIELD
        }
        return crName.isNotBlank() &&
        crAllSum.isNotBlank() &&
        crPerc.isNotBlank() &&
        crPeriod.isNotBlank() &&
        crFirstPay.isNotBlank()
    }

    private fun processCreditNotAuth(
        crName: String,
        crAllSum: Float,
        crPerc: Int,
        crPeriod: Int,
        crFirstPay: Float,
        ) {
        creditViewModel.addCreditNotAuth(
            crName = crName,
            crAllSum = crAllSum,
            crPeriod = crPeriod,
            crPerc = crPerc,
            crFirstPay = crFirstPay,
        )
        creditViewModel.curCredit.observe(viewLifecycleOwner) {
            val action = AddCreditFragmentDirections
                .actionAddCreditFragmentToCreditFragment(
                    creditViewModel.curCredit.value!!,
                    false
                )
            findNavController()
                .navigate(action)
        }
    }

    private fun processCreditAuth(
        crName: String,
        crAllSum: Float,
        crMonthPay: Float,
        crPerc: Int,
        crPercSum: Float,
        crPeriod: Int,
        crFirstPay: Float,
        crTotalSum: Float
    ) {
        creditViewModel.addCredit(
            crName = crName,
            crAllSum = crAllSum,
            crMonthPay = crMonthPay,
            crPerc = crPerc,
            crPercSum = crPercSum,
            crPeriod = crPeriod,
            crFirstPay = crFirstPay,
            crTotalSum = crTotalSum,
        )
        creditViewModel.curCredit.observe(viewLifecycleOwner) {
            val action = AddCreditFragmentDirections
                .actionAddCreditFragmentToCreditFragment(
                    creditViewModel.curCredit.value!!,
                    false
                )
            findNavController()
                .navigate(action)
        }
    }

    private fun stopAnim() {
        binding.calculateCreditButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner)
        binding.calculateCreditButton.revertAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.calculateCreditButton.dispose()
    }
}