package ru.vsu.cs.tp.richfamily.view.operation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.vsu.cs.tp.richfamily.databinding.FragmentAddOperationBinding

class AddOperationFragment : Fragment() {

    private lateinit var binding: FragmentAddOperationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddOperationBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }
}