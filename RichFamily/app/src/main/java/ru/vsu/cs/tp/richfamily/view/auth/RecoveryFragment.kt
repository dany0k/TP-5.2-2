package ru.vsu.cs.tp.richfamily.view.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.vsu.cs.tp.richfamily.databinding.FragmentRecoveryBinding

class RecoveryFragment : Fragment() {
    private lateinit var binding: FragmentRecoveryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecoveryBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }
}