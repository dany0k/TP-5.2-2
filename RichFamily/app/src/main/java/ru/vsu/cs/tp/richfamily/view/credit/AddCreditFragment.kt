package ru.vsu.cs.tp.richfamily.view.credit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.vsu.cs.tp.richfamily.MainActivity
import ru.vsu.cs.tp.richfamily.api.service.CreditApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentAddCreditBinding
import ru.vsu.cs.tp.richfamily.repository.CreditRepository
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.calculateCreditButton.setOnClickListener {
            with(binding) {
                if (token.isNotBlank()) {
                    creditViewModel.addCredit(
                        crName = crNameEt.text.toString(),
                        crAllSum = crSumEt.text.toString().toFloat(),
                        crMonthPay = 0F,
                        crPerc = crPercEt.text.toString().toInt(),
                        crPercSum = 0F,
                        crPeriod = crPerionEt.text.toString().toInt(),
                        crFirstPay = crFirstPayEt.text.toString().toFloat(),
                        crTotalSum = 0F,
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
                } else {
                    creditViewModel.addCreditNotAuth(
                        crName = crNameEt.text.toString(),
                        crAllSum = crSumEt.text.toString().toFloat(),
                        crPeriod = crPerionEt.text.toString().toInt(),
                        crPerc = crPercEt.text.toString().toInt(),
                        crFirstPay = crFirstPayEt.text.toString().toFloat(),
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

            }
        }
    }
}