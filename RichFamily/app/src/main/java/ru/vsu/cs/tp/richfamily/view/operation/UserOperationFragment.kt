package ru.vsu.cs.tp.richfamily.view.operation

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
import ru.vsu.cs.tp.richfamily.adapter.OperationRVAdapter
import ru.vsu.cs.tp.richfamily.databinding.FragmentUserOperationBinding

class UserOperationFragment : Fragment() {

    private lateinit var binding: FragmentUserOperationBinding
    private lateinit var adapter: OperationRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserOperationBinding.inflate(
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
        getOperations()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getOperations() {
        CoroutineScope(Dispatchers.IO).launch {
            requireActivity().runOnUiThread {
            }
        }
    }

    private fun initRcView() = with(binding) {
        operationsRv.layoutManager = LinearLayoutManager(context)
        operationsRv.adapter = adapter
    }
}