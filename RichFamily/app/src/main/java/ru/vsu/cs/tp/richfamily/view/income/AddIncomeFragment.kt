package ru.vsu.cs.tp.richfamily.view.income

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.databinding.FragmentAddIncomeBinding

class AddIncomeFragment: Fragment() {

    private lateinit var binding: FragmentAddIncomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddIncomeBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_add_income, container, false)
    }
}