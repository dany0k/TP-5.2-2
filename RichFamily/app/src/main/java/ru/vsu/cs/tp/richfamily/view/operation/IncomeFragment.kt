package ru.vsu.cs.tp.richfamily.view.operation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.*
import ru.vsu.cs.tp.richfamily.api.model.Income
import ru.vsu.cs.tp.richfamily.app.App
import ru.vsu.cs.tp.richfamily.databinding.FragmentIncomeBinding
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class IncomeFragment :
    Fragment(),
    IncomeClickDeleteInterface,
    IncomeClickEditInterface {
    private lateinit var adapter: IncomeRVAdapter

    lateinit var binding: FragmentIncomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIncomeBinding.inflate(inflater, container, false)
        binding.fab.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_incomeFragment_to_addOperationFragment)
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.initRetrofit()
        initRcView()
        getCons()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCons() {
        CoroutineScope(Dispatchers.IO).launch {
//            val list = App.serviceAPI.getWallets(loginViewModel.token.value!!)
            val list = mutableListOf(
                Income(0, "Доход 1", 1000f, LocalDate.now(), LocalTime.now().truncatedTo(
                    ChronoUnit.MINUTES)),
                Income(0, "Доход 2", 299f, LocalDate.now(), LocalTime.now().truncatedTo(
                    ChronoUnit.MINUTES)),
            )
            requireActivity().runOnUiThread {
                adapter.submitList(list)
            }
        }
    }

    private fun initRcView() = with(binding) {
        adapter = IncomeRVAdapter(
            this@IncomeFragment,
            this@IncomeFragment
        )
        walletsRv.layoutManager = LinearLayoutManager(context)
        walletsRv.adapter = adapter
    }

    override fun onDeleteIconClick(id: Int) {
    }

    override fun onEditIconClick(id: Int) {
    }
}