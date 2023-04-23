package ru.vsu.cs.tp.richfamily.view.credit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.databinding.FragmentAddCreditBinding
import ru.vsu.cs.tp.richfamily.databinding.FragmentCreditBinding

class AddCreditFragment : Fragment() {

    private lateinit var binding: FragmentAddCreditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCreditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.calculateCreditButton.setOnClickListener {
            findNavController()
                .navigate(R.id.action_addCreditFragment_to_creditFragment)
        }
    }
}