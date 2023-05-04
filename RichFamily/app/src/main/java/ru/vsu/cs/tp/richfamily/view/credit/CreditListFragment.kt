package ru.vsu.cs.tp.richfamily.view.credit

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.CreditClickDeleteInterface
import ru.vsu.cs.tp.richfamily.adapter.CreditClickItemInterface
import ru.vsu.cs.tp.richfamily.adapter.CreditRVAdapter
import ru.vsu.cs.tp.richfamily.api.model.Credit
import ru.vsu.cs.tp.richfamily.app.App
import ru.vsu.cs.tp.richfamily.databinding.FragmentCreditListBinding

class CreditListFragment :
    Fragment(),
    CreditClickDeleteInterface,
    CreditClickItemInterface {

    private lateinit var binding: FragmentCreditListBinding
    private lateinit var adapter: CreditRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreditListBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.calculateCreditButton.setOnClickListener {
            findNavController().navigate(R.id.action_creditListFragment_to_addCreditFragment)
        }
        initRcView()
        getCredits()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCredits() {
        CoroutineScope(Dispatchers.IO).launch {
//            val list = App.serviceAPI.getWallets(loginViewModel.token.value!!)
            val list = mutableListOf(
                Credit(0, "Кредит 1", 12, 1000f, 100000f, 100),
                Credit(0, "Кредит 1", 20, 2000f, 150000f, 100),
                Credit(0, "Кредит 1", 13, 10000f, 1000000f, 100)
            )
            requireActivity().runOnUiThread {
                adapter.submitList(list)
            }
        }
    }

    private fun initRcView() = with(binding) {
        adapter = CreditRVAdapter(
            this@CreditListFragment,
            this@CreditListFragment)
        creditsRv.layoutManager = LinearLayoutManager(context)
        creditsRv.adapter = adapter
    }

    override fun onDeleteIconClick(id: Int) {
    }

    override fun onItemClick(id: Int) {
        findNavController().navigate(R.id.action_creditListFragment_to_creditFragment)
    }
}