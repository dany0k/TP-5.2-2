package ru.vsu.cs.tp.richfamily.view.template

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.databinding.FragmentAddTemplateBinding

class AddTemplateFragment : Fragment() {

    private lateinit var binding: FragmentAddTemplateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTemplateBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_add_template, container, false)
    }
}