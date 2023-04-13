package ru.vsu.cs.tp.richfamily.ui.credit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.vsu.cs.tp.richfamily.databinding.FragmentCreditBinding

class CreditFragment : Fragment() {

    private lateinit var binding: FragmentCreditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreditBinding.inflate(inflater, container, false)
        return binding.root
    }
}