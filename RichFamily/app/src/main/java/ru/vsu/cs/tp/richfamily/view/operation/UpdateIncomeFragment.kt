package ru.vsu.cs.tp.richfamily.view.operation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.databinding.FragmentUpdateIncomeBinding

class UpdateIncomeFragment : Fragment() {

    private lateinit var binding: FragmentUpdateIncomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentUpdateIncomeBinding.inflate(
           inflater,
           container,
           false
       )
        return inflater.inflate(R.layout.fragment_update_income, container, false)
    }
}