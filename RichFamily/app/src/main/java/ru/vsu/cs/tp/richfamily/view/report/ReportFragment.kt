package ru.vsu.cs.tp.richfamily.view.report

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vsu.cs.tp.richfamily.adapter.*
import ru.vsu.cs.tp.richfamily.databinding.FragmentReportBinding

class ReportFragment : Fragment() {
    private lateinit var adapter: OperationRVAdapter

    private lateinit var binding: FragmentReportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(
            inflater,
            container,
            false
        )

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
            requireActivity().runOnUiThread {
            }
        }
    }

    private fun initRcView() = with(binding) {
        operationsRv.layoutManager = LinearLayoutManager(context)
        operationsRv.adapter = adapter
    }
}