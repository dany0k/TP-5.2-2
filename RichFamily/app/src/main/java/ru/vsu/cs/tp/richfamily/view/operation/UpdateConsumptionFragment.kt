package ru.vsu.cs.tp.richfamily.view.operation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.vsu.cs.tp.richfamily.databinding.FragmentUpdateConsumptionBinding

class UpdateConsumptionFragment : Fragment() {

    private lateinit var binding: FragmentUpdateConsumptionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateConsumptionBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }
}