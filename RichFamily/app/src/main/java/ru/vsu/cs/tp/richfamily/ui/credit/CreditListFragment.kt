package ru.vsu.cs.tp.richfamily.ui.credit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.ui.NavigationUI
import ru.vsu.cs.tp.richfamily.databinding.FragmentCreditListBinding

class CreditListFragment : Fragment() {

    private lateinit var binding: FragmentCreditListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreditListBinding.inflate(inflater, container, false)

        return binding.root
    }
}