package ru.vsu.cs.tp.richfamily.view.operation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.cs.tp.richfamily.MainActivity
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.OperationClickDeleteInterface
import ru.vsu.cs.tp.richfamily.adapter.OperationClickEditInterface
import ru.vsu.cs.tp.richfamily.adapter.OperationRVAdapter
import ru.vsu.cs.tp.richfamily.api.service.OperationApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentIncomeBinding
import ru.vsu.cs.tp.richfamily.repository.OperationRepository
import ru.vsu.cs.tp.richfamily.viewmodel.OperationViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.AnyViewModelFactory

class IncomeFragment :
    Fragment(),
    OperationClickDeleteInterface,
    OperationClickEditInterface {
    private lateinit var adapter: OperationRVAdapter
    private lateinit var opViewModel: OperationViewModel
    private lateinit var binding: FragmentIncomeBinding
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIncomeBinding.inflate(inflater, container, false)
        initRcView()
        token = MainActivity.getToken()
        if (token.isNotEmpty()) {
            val operationApi = OperationApi.getOperationApi()!!
            val opRepository = OperationRepository(operationApi = operationApi, token = token)
            opViewModel = ViewModelProvider(
                requireActivity(),
                AnyViewModelFactory(
                    repository = opRepository,
                    token = token
                )
            )[OperationViewModel::class.java]
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (token.isNotBlank()) {
            opViewModel.inList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
            opViewModel.errorMessage.observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
            opViewModel.loading.observe(viewLifecycleOwner) {
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
            opViewModel.getAllOperations()
        }

        binding.fab.setOnClickListener {
            if (token.isEmpty()) {
                Navigation.findNavController(it)
                    .navigate(R.id.action_incomeFragment_to_registrationFragment)
            } else {
                Navigation.findNavController(it)
                    .navigate(R.id.action_incomeFragment_to_addOperationFragment)
            }
        }

        binding.incRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    binding.fab.hide()
                } else if (dy < 0) {
                    binding.fab.show()
                }
            }
        })
    }

    private fun initRcView() = with(binding) {
        adapter = OperationRVAdapter(
            this@IncomeFragment,
            this@IncomeFragment,
            false
        )
        incRv.layoutManager = LinearLayoutManager(context)
        incRv.adapter = adapter
    }

    override fun onDeleteIconClick(id: Int) {
        opViewModel.deleteOperation(id = id)
        binding.fab.show()
    }

    override fun onEditIconClick(id: Int) {
        opViewModel.getOperationById(id = id)
        findNavController()
            .navigate(R.id.action_incomeFragment_to_updateOperationFragment)
    }
}