package ru.vsu.cs.tp.richfamily.view.template

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.databinding.FragmentUpdateTemplateBinding

class UpdateTemplateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateTemplateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateTemplateBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }
}