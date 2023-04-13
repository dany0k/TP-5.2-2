package ru.vsu.cs.tp.richfamily.view.account

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import ru.vsu.cs.tp.richfamily.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(
            inflater,
            container,
            false)
        return binding.root
    }

}