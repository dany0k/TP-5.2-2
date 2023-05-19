package ru.vsu.cs.tp.richfamily.view.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.vsu.cs.tp.richfamily.databinding.FragmentEditAccountBinding

class EditAccountFragment : Fragment() {

    private lateinit var binding: FragmentEditAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditAccountBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }
}