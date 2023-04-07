package ru.vsu.cs.tp.richfamily.ui.wallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.databinding.FragmentWalletBinding

class WalletFragment : Fragment() {

    lateinit var binding: FragmentWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_wallet,
            container,
            false
        )
        return binding.root
    }
}