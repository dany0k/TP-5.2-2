package ru.vsu.cs.tp.richfamily.ui.consumption

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.databinding.FragmentAddConsumptionBinding

class AddConsumptionFragment : Fragment() {

    private lateinit var binding: FragmentAddConsumptionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddConsumptionBinding.inflate(inflater, container, false)
        return binding.root
    }
}