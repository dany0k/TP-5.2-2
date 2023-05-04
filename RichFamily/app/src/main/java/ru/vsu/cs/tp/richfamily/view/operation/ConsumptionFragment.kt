package ru.vsu.cs.tp.richfamily.view.operation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.*
import ru.vsu.cs.tp.richfamily.api.model.Consumption
import ru.vsu.cs.tp.richfamily.app.App
import ru.vsu.cs.tp.richfamily.databinding.FragmentConsumptionBinding
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class ConsumptionFragment :
    Fragment(),
    ConsumptionClickDeleteInterface,
    ConsumptionClickEditInterface {
    private lateinit var adapter: ConsumptionRVAdapter

    lateinit var binding: FragmentConsumptionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConsumptionBinding.inflate(inflater, container, false)
        binding.fab.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_consumptionFragment_to_addOperationFragment)
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        getCons()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCons() {
        CoroutineScope(Dispatchers.IO).launch {
//            val list = App.serviceAPI.getWallets(loginViewModel.token.value!!)
            val list = mutableListOf(
                Consumption(0, "Расход 1", 1000f, LocalDate.now(), LocalTime.now().truncatedTo(ChronoUnit.MINUTES)),
                Consumption(0, "Расход 2", 299f, LocalDate.now(), LocalTime.now().truncatedTo(ChronoUnit.MINUTES)),
            )
            requireActivity().runOnUiThread {
                adapter.submitList(list)
            }
        }
    }

    private fun initRcView() = with(binding) {
        adapter = ConsumptionRVAdapter(
            this@ConsumptionFragment,
            this@ConsumptionFragment
        )
        walletsRv.layoutManager = LinearLayoutManager(context)
        walletsRv.adapter = adapter
    }

    override fun onDeleteIconClick(id: Int) {
    }

    override fun onEditIconClick(id: Int) {
        findNavController()
            .navigate(R.id.action_consumptionFragment_to_updateConsumptionFragment)
    }
}