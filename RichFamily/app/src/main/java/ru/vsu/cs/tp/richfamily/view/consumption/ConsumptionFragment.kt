package ru.vsu.cs.tp.richfamily.ui.consumption

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.databinding.FragmentConsumptionBinding

class ConsumptionFragment : Fragment() {

    lateinit var binding: FragmentConsumptionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConsumptionBinding.inflate(inflater, container, false)
        binding.fab.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_consumptionFragment_to_addConsumptionFragment)
        }
        return binding.root
    }
}