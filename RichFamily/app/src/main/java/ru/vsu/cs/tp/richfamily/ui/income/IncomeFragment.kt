package ru.vsu.cs.tp.richfamily.ui.income

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.vsu.cs.tp.richfamily.databinding.FragmentIncomeBinding

class IncomeFragment : Fragment() {

    lateinit var binding: FragmentIncomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }
}