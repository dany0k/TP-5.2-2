package ru.vsu.cs.tp.richfamily.view.credit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.databinding.FragmentCreditBinding

class CreditFragment : Fragment() {

    private lateinit var binding: FragmentCreditBinding
    private val args by navArgs<CreditFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreditBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCredit()
        binding.continueButton.setOnClickListener {
            binding.continueButton.startAnimation()
            findNavController()
                .navigate(R.id.action_creditFragment_to_creditListFragment)
            binding.continueButton.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner)
            binding.continueButton.revertAnimation()
        }
    }

    private fun setCredit() = with(binding) {
        val formattedStr = "%.3f"
        creditTotalTv.text = formattedStr.format(args.credit.cr_all_sum)
        percentageTv.text = args.credit.cr_percent.toString()
        periodTv.text = args.credit.cr_period.toString()
        crPercentsSumTv.text = formattedStr.format(args.credit.cr_percents_sum)
        crSumPlusPercentsTv.text = formattedStr.format(args.credit.cr_sum_plus_percents)
        monthlySumTv.text = formattedStr.format(args.credit.cr_month_pay)
        firstPayTv.text = formattedStr.format(args.credit.cr_first_pay)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.continueButton.dispose()
    }
}