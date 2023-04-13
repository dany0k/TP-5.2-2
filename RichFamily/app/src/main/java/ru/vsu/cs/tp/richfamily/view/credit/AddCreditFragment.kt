package ru.vsu.cs.tp.richfamily.view.credit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.vsu.cs.tp.richfamily.databinding.FragmentAddCreditBinding

class AddCreditFragment : Fragment() {

    private lateinit var binding: FragmentAddCreditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCreditBinding.inflate(inflater, container, false)
        return binding.root
    }

}